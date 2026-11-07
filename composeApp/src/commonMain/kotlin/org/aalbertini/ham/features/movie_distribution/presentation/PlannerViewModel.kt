package org.aalbertini.ham.features.movie_distribution.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.aalbertini.ham.features.movie_distribution.data.repository.ImportResult
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionDefaults
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionPlan
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ClearAllMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.DeleteMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ExportLibraryUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ImportLibraryUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.LoadMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.PlanDistributionUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.SaveMovieResultUseCase

/**
 * @property movie The movie being edited. A blank id means it has never been saved.
 * @property library Saved movies, most recently updated first.
 */
data class PlannerUiState(
    val movie: MovieResult,
    val plan: DistributionPlan,
    val library: List<MovieResult>,
) {
    val isSaved: Boolean get() = movie.id.isNotBlank()
}

/** One-off events for the snackbar. Text is resolved in the UI so it can be localized. */
sealed interface PlannerMessage {
    data class Saved(val title: String) : PlannerMessage

    /** @property discarded The unsaved draft this replaced, offered back with Undo. */
    data class Opened(val title: String, val discarded: MovieResult?) : PlannerMessage
    data class Deleted(val movie: MovieResult) : PlannerMessage
    data class Duplicated(val title: String) : PlannerMessage
    data object LibraryCleared : PlannerMessage

    /** @property discarded The unsaved draft this replaced, offered back with Undo. */
    data class NewMovie(val discarded: MovieResult?) : PlannerMessage
    data class Imported(val result: ImportResult) : PlannerMessage
    data object ImportFailed : PlannerMessage
}

/**
 * Single source of truth for the planner and the library.
 * Every edit replans immediately; once a movie is saved, edits are auto-saved after a short pause.
 */
class PlannerViewModel(
    private val loadMovies: LoadMovieResultsUseCase,
    private val saveMovie: SaveMovieResultUseCase,
    private val deleteMovie: DeleteMovieResultUseCase,
    private val clearMovies: ClearAllMovieResultsUseCase,
    private val exportLibrary: ExportLibraryUseCase,
    private val importLibrary: ImportLibraryUseCase,
    private val planDistribution: PlanDistributionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(stateFor(newMovieDraft(), loadMovies()))
    val state: StateFlow<PlannerUiState> = _state.asStateFlow()

    private val _messages = Channel<PlannerMessage>(Channel.BUFFERED)
    val messages: Flow<PlannerMessage> = _messages.receiveAsFlow()

    private var autoSaveJob: Job? = null

    /** Applies an edit to the current movie and replans. */
    fun edit(transform: (MovieResult) -> MovieResult) {
        _state.update { it.copy(movie = transform(it.movie)).replanned() }
        if (_state.value.isSaved) scheduleAutoSave()
    }

    fun save() {
        autoSaveJob?.cancel()
        val stored = saveMovie(_state.value.movie)
        _state.update { it.copy(movie = stored, library = loadMovies()) }
        send(PlannerMessage.Saved(stored.title))
    }

    fun newMovie() {
        // Already on an untouched draft: a second press must not push the Undo snackbar away.
        if (!_state.value.isSaved && _state.value.movie == newMovieDraft()) return
        flushAutoSave()
        val discarded = discardableDraft()
        _state.update { it.copy(movie = newMovieDraft()).replanned() }
        send(PlannerMessage.NewMovie(discarded))
    }

    fun open(id: String) {
        flushAutoSave()
        val movie = _state.value.library.firstOrNull { it.id == id } ?: return
        val discarded = discardableDraft()
        _state.update { it.copy(movie = movie).replanned() }
        send(PlannerMessage.Opened(movie.title, discarded))
    }

    /** Puts back an unsaved draft that [newMovie] or [open] replaced (snackbar Undo). */
    fun restoreDraft(draft: MovieResult) {
        flushAutoSave()
        _state.update { it.copy(movie = draft).replanned() }
    }

    fun duplicate(id: String, titleOf: (String) -> String) {
        flushAutoSave()
        val source = _state.value.library.firstOrNull { it.id == id } ?: return
        val copy = saveMovie(source.copy(id = "", title = titleOf(source.title), createdAt = 0))
        refreshLibrary()
        send(PlannerMessage.Duplicated(copy.title))
    }

    fun delete(id: String) {
        val saved = _state.value.library.firstOrNull { it.id == id } ?: return
        val isOpen = _state.value.movie.id == id
        // A pending auto-save would write the open movie back under a new id after the delete.
        if (isOpen) autoSaveJob?.cancel()
        deleteMovie(id)
        _state.update { state ->
            // The open movie stays on screen as an unsaved draft.
            val current = if (isOpen) state.movie.copy(id = "") else state.movie
            state.copy(movie = current, library = loadMovies())
        }
        // Undo brings back the latest edits of the open movie, not the last auto-saved copy.
        send(PlannerMessage.Deleted(if (isOpen) _state.value.movie.copy(id = id) else saved))
    }

    fun restore(movie: MovieResult) {
        val draft = _state.value.movie
        // The deleted movie is still open as a draft: keep what was typed since the delete.
        val isOpenDraft = draft.id.isBlank() && movie.createdAt > 0 && draft.createdAt == movie.createdAt
        val stored = saveMovie(if (isOpenDraft) draft.copy(id = movie.id) else movie)
        _state.update { state ->
            state.copy(movie = if (isOpenDraft) stored else state.movie, library = loadMovies()).replanned()
        }
    }

    fun clearLibrary() {
        autoSaveJob?.cancel()
        clearMovies()
        _state.update { it.copy(movie = it.movie.copy(id = ""), library = emptyList()) }
        send(PlannerMessage.LibraryCleared)
    }

    /** The whole library as JSON text, for the clipboard. */
    fun exportText(): String {
        flushAutoSave()
        return exportLibrary()
    }

    fun import(text: String) {
        flushAutoSave()
        val result = importLibrary(text)
        if (result == null) {
            send(PlannerMessage.ImportFailed)
            return
        }
        _state.update { state ->
            val library = loadMovies()
            // The open movie may have been replaced by a newer imported copy.
            val current = library.firstOrNull { it.id == state.movie.id && state.isSaved } ?: state.movie
            state.copy(movie = current, library = library).replanned()
        }
        send(PlannerMessage.Imported(result))
    }

    override fun onCleared() {
        flushAutoSave()
    }

    private fun scheduleAutoSave() {
        autoSaveJob?.cancel()
        val id = _state.value.movie.id
        autoSaveJob = viewModelScope.launch {
            delay(AUTO_SAVE_DELAY_MS)
            autoSaveNow(id)
        }
    }

    /** Writes a pending auto-save right away instead of dropping it (switching movie within the delay). */
    private fun flushAutoSave() {
        val job = autoSaveJob ?: return
        autoSaveJob = null
        if (!job.isActive) return
        job.cancel()
        autoSaveNow(_state.value.movie.id)
    }

    private fun autoSaveNow(id: String) {
        val movie = _state.value.movie
        if (id.isBlank() || movie.id != id) return
        val stored = saveMovie(movie)
        _state.update { it.copy(movie = it.movie.copy(updatedAt = stored.updatedAt), library = loadMovies()) }
    }

    /** The current movie if it is an unsaved draft with edits, i.e. something New/Open would lose. */
    private fun discardableDraft(): MovieResult? {
        val movie = _state.value.movie
        return movie.takeIf { !_state.value.isSaved && it != newMovieDraft() }
    }

    private fun refreshLibrary() = _state.update { it.copy(library = loadMovies()) }

    private fun send(message: PlannerMessage) {
        _messages.trySend(message)
    }

    private fun PlannerUiState.replanned() = copy(plan = planDistribution(movie))

    private fun stateFor(movie: MovieResult, library: List<MovieResult>) =
        PlannerUiState(movie = movie, plan = planDistribution(movie), library = library)

    private companion object {
        const val AUTO_SAVE_DELAY_MS = 600L

        fun newMovieDraft() = MovieResult(
            id = "",
            title = "",
            commercialScore = DistributionDefaults.DEFAULT_COMMERCIAL_SCORE,
            numberOfScreenings = DistributionDefaults.DEFAULT_OWN_SCREENINGS.toDouble(),
            method = DistributionDefaults.DEFAULT_METHOD,
        )
    }
}
