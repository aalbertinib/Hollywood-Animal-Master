package org.aalbertini.ham

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.aalbertini.ham.features.movie_distribution.data.data_source.MovieResultDataSource
import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ClearAllMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.DeleteMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ExportLibraryUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ImportLibraryUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.LoadMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.PlanDistributionUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.SaveMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.presentation.PlannerMessage
import org.aalbertini.ham.features.movie_distribution.presentation.PlannerViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PlannerViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel(store: InMemoryStore = InMemoryStore()): PlannerViewModel {
        val repository = MovieResultRepository(MovieResultDataSource(store))
        return PlannerViewModel(
            loadMovies = LoadMovieResultsUseCase(repository),
            saveMovie = SaveMovieResultUseCase(repository),
            deleteMovie = DeleteMovieResultUseCase(repository),
            clearMovies = ClearAllMovieResultsUseCase(repository),
            exportLibrary = ExportLibraryUseCase(repository),
            importLibrary = ImportLibraryUseCase(repository),
            planDistribution = PlanDistributionUseCase(),
        )
    }

    private fun TestScope.messagesOf(vm: PlannerViewModel): List<PlannerMessage> {
        val messages = mutableListOf<PlannerMessage>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.messages.toList(messages) }
        return messages
    }

    private fun PlannerViewModel.titles() = state.value.library.map { it.title }

    @Test
    fun savedMovie_autoSavesAfterAPause() = runTest(dispatcher) {
        val vm = viewModel()
        vm.edit { it.copy(title = "A") }
        vm.save()
        vm.edit { it.copy(title = "A2") }
        advanceTimeBy(500)
        assertEquals(listOf("A"), vm.titles())
        advanceTimeBy(200)
        runCurrent()
        assertEquals(listOf("A2"), vm.titles())
    }

    @Test
    fun unsavedDraft_isNeverAutoSaved() = runTest(dispatcher) {
        val vm = viewModel()
        vm.edit { it.copy(title = "Draft") }
        advanceUntilIdle()
        assertEquals(emptyList(), vm.titles())
    }

    @Test
    fun deletingOpenMovie_withPendingAutoSave_doesNotBringItBack() = runTest(dispatcher) {
        val vm = viewModel()
        vm.edit { it.copy(title = "A") }
        vm.save()
        vm.edit { it.copy(title = "A2") }
        vm.delete(vm.state.value.movie.id)
        advanceUntilIdle()
        assertEquals(emptyList(), vm.titles())
        assertTrue(!vm.state.value.isSaved)
    }

    @Test
    fun undoDelete_keepsWhatWasTypedAfterTheDelete() = runTest(dispatcher) {
        val vm = viewModel()
        val messages = messagesOf(vm)
        vm.edit { it.copy(title = "A") }
        vm.save()
        val id = vm.state.value.movie.id
        vm.delete(id)
        vm.edit { it.copy(title = "A edited") }
        val deleted = assertIs<PlannerMessage.Deleted>(messages.last())
        vm.restore(deleted.movie)
        assertEquals(listOf("A edited"), vm.titles())
        assertEquals(id, vm.state.value.movie.id)
    }

    @Test
    fun undoDelete_ofAnotherMovie_leavesTheOpenOneAlone() = runTest(dispatcher) {
        val vm = viewModel()
        val messages = messagesOf(vm)
        vm.edit { it.copy(title = "A") }
        vm.save()
        val a = vm.state.value.movie
        vm.newMovie()
        vm.edit { it.copy(title = "B") }
        vm.save()
        vm.delete(a.id)
        vm.restore(assertIs<PlannerMessage.Deleted>(messages.last()).movie)
        assertEquals(setOf("A", "B"), vm.titles().toSet())
        assertEquals("B", vm.state.value.movie.title)
    }

    @Test
    fun newMovie_writesAPendingAutoSaveInsteadOfDroppingIt() = runTest(dispatcher) {
        val vm = viewModel()
        vm.edit { it.copy(title = "A") }
        vm.save()
        vm.edit { it.copy(title = "A2") }
        vm.newMovie() // within the auto-save delay
        assertEquals(listOf("A2"), vm.titles())
    }

    @Test
    fun open_writesAPendingAutoSaveInsteadOfDroppingIt() = runTest(dispatcher) {
        val vm = viewModel()
        vm.edit { it.copy(title = "A") }
        vm.save()
        val a = vm.state.value.movie.id
        vm.newMovie()
        vm.edit { it.copy(title = "B") }
        vm.save()
        vm.edit { it.copy(commercialScore = 9.0) }
        vm.open(a)
        assertEquals(9.0, vm.state.value.library.first { it.title == "B" }.commercialScore)
    }

    @Test
    fun newMovie_overAnEditedDraft_offersItBack() = runTest(dispatcher) {
        val vm = viewModel()
        val messages = messagesOf(vm)
        vm.edit { it.copy(title = "Draft", commercialScore = 7.0) }
        vm.newMovie()
        val message = assertIs<PlannerMessage.NewMovie>(messages.last())
        assertEquals("Draft", message.discarded?.title)
        vm.restoreDraft(message.discarded!!)
        assertEquals("Draft", vm.state.value.movie.title)
        assertEquals(7.0, vm.state.value.movie.commercialScore)
    }

    @Test
    fun newMovie_overAnUntouchedDraft_doesNothing() = runTest(dispatcher) {
        val vm = viewModel()
        val messages = messagesOf(vm)
        vm.newMovie()
        assertEquals(emptyList(), messages)
    }

    @Test
    fun newMovie_pressedTwice_keepsTheUndoForTheDraft() = runTest(dispatcher) {
        val vm = viewModel()
        val messages = messagesOf(vm)
        vm.edit { it.copy(title = "Draft") }
        vm.newMovie()
        vm.newMovie()
        assertEquals(1, messages.size)
        assertEquals("Draft", assertIs<PlannerMessage.NewMovie>(messages.single()).discarded?.title)
    }

    @Test
    fun newMovie_fromASavedMovie_hasNothingToUndo() = runTest(dispatcher) {
        val vm = viewModel()
        val messages = messagesOf(vm)
        vm.edit { it.copy(title = "A") }
        vm.save()
        vm.newMovie()
        assertNull(assertIs<PlannerMessage.NewMovie>(messages.last()).discarded)
    }

    @Test
    fun open_overAnEditedDraft_offersItBack() = runTest(dispatcher) {
        val store = InMemoryStore()
        val first = viewModel(store)
        first.edit { it.copy(title = "Saved") }
        first.save()
        val vm = viewModel(store)
        val messages = messagesOf(vm)
        vm.edit { it.copy(title = "Draft") }
        vm.open(vm.state.value.library.single().id)
        assertEquals("Draft", assertIs<PlannerMessage.Opened>(messages.last()).discarded?.title)
    }

    @Test
    fun export_thenImportElsewhere_bringsTheLibraryOver() = runTest(dispatcher) {
        val source = viewModel()
        source.edit { it.copy(title = "A") }
        source.save()
        val target = viewModel()
        val messages = messagesOf(target)
        target.import(source.exportText())
        assertEquals(listOf("A"), target.titles())
        assertEquals(1, assertIs<PlannerMessage.Imported>(messages.last()).result.added)
    }

    @Test
    fun import_ofRandomText_reportsFailure() = runTest(dispatcher) {
        val vm = viewModel()
        val messages = messagesOf(vm)
        vm.import("not a library")
        assertIs<PlannerMessage.ImportFailed>(messages.last())
    }

    @Test
    fun clearLibrary_withPendingAutoSave_staysEmpty() = runTest(dispatcher) {
        val vm = viewModel()
        vm.edit { it.copy(title = "A") }
        vm.save()
        vm.edit { it.copy(title = "A2") }
        vm.clearLibrary()
        advanceUntilIdle()
        assertEquals(emptyList(), vm.titles())
    }
}
