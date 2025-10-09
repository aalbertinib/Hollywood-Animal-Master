package org.aalbertini.ham.ui.state

/**
 * UI Events for the Calculator screen
 */
sealed class MovieDistributionUiEvent {
    data class UpdateP1Input(val value: String) : MovieDistributionUiEvent()
    data class UpdateP2Input(val value: String) : MovieDistributionUiEvent()
    data class UpdateEditableTitle(val title: String) : MovieDistributionUiEvent()
    data object RevertTitle : MovieDistributionUiEvent()
    data object AutoSaveMovieResult : MovieDistributionUiEvent()
    data class SaveMovieResult(val title: String) : MovieDistributionUiEvent()
    data object OverwriteConflictingMovie : MovieDistributionUiEvent()
    data object KeepExistingMovie : MovieDistributionUiEvent()
    data object DismissParameterConflict : MovieDistributionUiEvent()
    data class LoadMovieResult(val movieResultId: String) : MovieDistributionUiEvent()
    data class UpdateMovieResult(val id: String, val title: String, val p1: Double, val p2: Double) : MovieDistributionUiEvent()
    data class DeleteMovieResult(val movieResultId: String) : MovieDistributionUiEvent()
    data object ClearAllMovieResults : MovieDistributionUiEvent()
    data object NewMovieResult : MovieDistributionUiEvent()
    data object ToggleResultsExpand : MovieDistributionUiEvent()
    data object ToggleSavedExpand : MovieDistributionUiEvent()
    data object DismissNotification : MovieDistributionUiEvent()
    data class CopyResults(val results: List<Long>) : MovieDistributionUiEvent()
}
