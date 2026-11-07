package org.aalbertini.ham

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.aalbertini.ham.core.util.format.signedGrouped
import org.aalbertini.ham.di.appModules
import org.aalbertini.ham.features.movie_distribution.data.data_source.MovieResultDataSource
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieComparison
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.settings.data.SettingsRepository
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The whole app, with every repository on an in-memory store so the real save files
 * (~/.hollywood_animal_master) are never touched.
 */
@OptIn(ExperimentalTestApi::class)
class AppUiTest {

    private val store = InMemoryStore()

    @BeforeTest
    fun setUp() {
        startKoin {
            allowOverride(true)
            modules(
                appModules + module {
                    single { MovieResultDataSource(store) }
                    single { SettingsRepository(store) }
                }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    /** A real window provides the ViewModelStoreOwner; the test host doesn't. */
    @Composable
    private fun TestApp() {
        val owner = remember {
            object : ViewModelStoreOwner {
                override val viewModelStore = ViewModelStore()
            }
        }
        CompositionLocalProvider(LocalViewModelStoreOwner provides owner) { App() }
    }

    private fun SemanticsNodeInteractionsProvider.titleField() =
        onNode(hasSetTextAction() and hasText("Movie title", substring = true))

    @Test
    fun newMovie_overAnUnsavedDraft_canBeUndone() = runDesktopComposeUiTest(width = 1280, height = 900) {
        setContent { TestApp() }
        titleField().performTextInput("Draft test")
        onNodeWithContentDescription("New movie").performClick()
        onNodeWithText("Started a new movie. The unsaved one was closed.").assertExists()

        onNodeWithText("Undo").performClick()
        waitForIdle()
        titleField().assert(hasText("Draft test"))
    }

    @Test
    fun deleteFromLibrary_canBeUndone() = runDesktopComposeUiTest(width = 1280, height = 900) {
        setContent { TestApp() }
        titleField().performTextInput("Keeper")
        onNodeWithText("Save").performClick()
        onNodeWithText("Library").performClick()
        onNodeWithText("Open").assertExists() // the open movie is tagged, not only tinted

        // The movie's own menu, not the top bar's library menu.
        onNode(hasContentDescription("More options") and hasAnyAncestor(hasText("Keeper", substring = true)))
            .performClick()
        onNodeWithText("Delete").performClick()
        onNodeWithText("No saved movies yet").assertExists()

        onNodeWithText("Undo").performClick()
        waitForIdle()
        onNodeWithText("Keeper").assertExists()
        assertEquals(1, MovieResultDataSource(store).loadAll().size)
    }

    @Test
    fun importDialog_mergesAnExportedLibrary() = runDesktopComposeUiTest(width = 1280, height = 900) {
        val exported = MovieResultDataSource(InMemoryStore()).encode(
            listOf(
                MovieResult(
                    id = "x1", title = "From my laptop", commercialScore = 7.0, numberOfScreenings = 0.0,
                    updatedAt = 1,
                )
            )
        )
        setContent { TestApp() }
        onNodeWithText("Library").performClick()
        onNodeWithText("Import a library").performClick() // empty-state button
        onNode(hasSetTextAction() and hasText("Exported library", substring = true)).performTextInput(exported)
        onNodeWithText("Import").performClick()

        onNodeWithText("Imported: 1 new, 0 updated").assertExists()
        onNodeWithText("From my laptop").assertExists()
    }

    private fun saved(title: String, commercial: Double, updatedAt: Long) = MovieResult(
        id = title.lowercase(),
        title = title,
        commercialScore = commercial,
        numberOfScreenings = 3200.0,
        createdAt = updatedAt,
        updatedAt = updatedAt,
    )

    @Test
    fun compare_twoMovies_showsTheDifferenceFromTheFirstPicked() = runDesktopComposeUiTest(width = 1280, height = 1400) {
        val alpha = saved("Alpha", 5.0, updatedAt = 2)
        val beta = saved("Beta", 8.0, updatedAt = 1)
        MovieResultDataSource(store).saveAll(listOf(alpha, beta))
        setContent { TestApp() }

        onNodeWithText("Library").performClick()
        onNodeWithContentDescription("Compare movies").performClick()
        onNodeWithText("Select movies").assertExists()
        // Pick Beta first: it becomes the reference.
        onNodeWithText("Beta").performClick()
        onNodeWithText("Alpha").performClick()
        onNodeWithText("2 selected").assertExists()
        onNodeWithText("Compare").performClick()

        // Both tables head their columns with the titles; Beta, picked first, is the reference.
        onAllNodes(hasText("Beta") and hasText("Reference")).assertCountEquals(2)
        onAllNodes(hasText("Alpha") and hasText("Reference")).assertCountEquals(0)
        val expected = MovieComparison(listOf(beta, alpha))
        onNodeWithText(expected.totalRentDelta(1).signedGrouped()).assertExists()
        onNodeWithText("Week 8").assertExists()

        // Back returns to the picking step with the same movies, then Cancel leaves it.
        onNodeWithContentDescription("Back").performClick()
        onNodeWithText("2 selected").assertExists()
        onNodeWithContentDescription("Cancel selection").performClick()
        onNodeWithContentDescription("Compare movies").assertExists()
    }

    @Test
    fun compare_stopsAtFourMovies() = runDesktopComposeUiTest(width = 1280, height = 1400) {
        val names = listOf("Alpha", "Beta", "Gamma", "Delta", "Echo")
        MovieResultDataSource(store).saveAll(names.mapIndexed { i, name -> saved(name, 5.0, updatedAt = 10L - i) })
        setContent { TestApp() }

        onNodeWithText("Library").performClick()
        onNodeWithContentDescription("Compare movies").performClick()
        names.take(4).forEach { onNodeWithText(it).performClick() }
        onNodeWithText("4 selected").assertExists()
        onNode(isToggleable() and hasText("Echo", substring = true)).assertIsNotEnabled()
        // Unpicking one frees a slot again.
        onNodeWithText("Alpha").performClick()
        onNode(isToggleable() and hasText("Echo", substring = true)).assertIsEnabled()
    }

    @Test
    fun compare_needsTwoMovies() = runDesktopComposeUiTest(width = 1280, height = 1400) {
        MovieResultDataSource(store).saveAll(listOf(saved("Alpha", 5.0, 2), saved("Beta", 6.0, 1)))
        setContent { TestApp() }
        onNodeWithText("Library").performClick()
        onNodeWithContentDescription("Compare movies").performClick()
        onNodeWithText("Alpha").performClick()
        onNodeWithText("Compare").assertIsNotEnabled()
    }

    @Test
    fun compare_movieTitle_opensItInThePlanner() = runDesktopComposeUiTest(width = 1280, height = 1400) {
        MovieResultDataSource(store).saveAll(listOf(saved("Alpha", 5.0, 2), saved("Beta", 6.0, 1)))
        setContent { TestApp() }
        onNodeWithText("Library").performClick()
        onNodeWithContentDescription("Compare movies").performClick()
        onNodeWithText("Alpha").performClick()
        onNodeWithText("Beta").performClick()
        onNodeWithText("Compare").performClick()

        // Titles head both tables; either opens the movie.
        onAllNodesWithText("Beta")[0].performClick()
        titleField().assert(hasText("Beta"))
    }
}
