package org.aalbertini.ham.ui.state

/**
 * UI Events for the Calculator screen
 */
sealed class MovieDistributionUiEvent {
    data class UpdateCommercialScoreInput(val value: String) : MovieDistributionUiEvent()
    data class UpdateAvailableSeatsInput(val value: String) : MovieDistributionUiEvent()
    data class UpdateAvailableSeatsOverride(val weekIndex: Int, val value: String) : MovieDistributionUiEvent()
    data class ClearAvailableSeatsOverride(val weekIndex: Int) : MovieDistributionUiEvent()
    data class UpdateEditableTitle(val title: String) : MovieDistributionUiEvent()
    data object RevertTitle : MovieDistributionUiEvent()
    data object RevertCommercialScore : MovieDistributionUiEvent()
    data object RevertAvailableSeats : MovieDistributionUiEvent()
    data object AutoSaveMovieResult : MovieDistributionUiEvent()
    data class SaveMovieResult(val title: String) : MovieDistributionUiEvent()
    data object OverwriteConflictingMovie : MovieDistributionUiEvent()
    data object KeepExistingMovie : MovieDistributionUiEvent()
    data object DismissParameterConflict : MovieDistributionUiEvent()
    data class LoadMovieResult(val movieResultId: String) : MovieDistributionUiEvent()
    data class UpdateMovieResult(val id: String, val title: String, val commercialScore: Double, val availableSeats: Double) : MovieDistributionUiEvent()
    data class DeleteMovieResult(val movieResultId: String) : MovieDistributionUiEvent()
    data object ClearAllMovieResults : MovieDistributionUiEvent()
    data object NewMovieResult : MovieDistributionUiEvent()
    data object ToggleResultsExpand : MovieDistributionUiEvent()
    data object ToggleSavedExpand : MovieDistributionUiEvent()
    data object DismissNotification : MovieDistributionUiEvent()
    data class CopyResults(val results: List<Long>) : MovieDistributionUiEvent()
}
