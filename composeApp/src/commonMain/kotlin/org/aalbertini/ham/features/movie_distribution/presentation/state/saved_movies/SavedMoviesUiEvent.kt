package org.aalbertini.ham.features.movie_distribution.presentation.state.saved_movies

/**
 * UI Events for the Saved Movies section
 */
sealed class SavedMoviesUiEvent {
    data object AutoSaveMovieResult : SavedMoviesUiEvent()
    data class SaveMovieResult(val title: String) : SavedMoviesUiEvent()
    data class LoadMovieResult(val movieResultId: String) : SavedMoviesUiEvent()
    data class UpdateMovieResult(
        val id: String,
        val title: String,
        val commercialScore: Double,
        val availableScreenings: Double
    ) : SavedMoviesUiEvent()
    data class DeleteMovieResult(val movieResultId: String) : SavedMoviesUiEvent()
    data object ClearAllMovieResults : SavedMoviesUiEvent()
    data object ToggleSavedExpand : SavedMoviesUiEvent()
    data object DismissNotification : SavedMoviesUiEvent()
    data object OverwriteConflictingMovie : SavedMoviesUiEvent()
    data object KeepExistingMovie : SavedMoviesUiEvent()
    data object DismissParameterConflict : SavedMoviesUiEvent()
}
