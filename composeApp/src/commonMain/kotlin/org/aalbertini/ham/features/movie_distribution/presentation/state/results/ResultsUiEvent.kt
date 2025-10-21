package org.aalbertini.ham.features.movie_distribution.presentation.state.results

/**
 * UI Events for the Results section
 */
sealed class ResultsUiEvent {
    data class UpdateAvailableScreeningsOverride(val weekIndex: Int, val value: String) : ResultsUiEvent()
    data class ClearAvailableScreeningsOverride(val weekIndex: Int) : ResultsUiEvent()
    data class UpdateWeekMultiplierOverride(val weekIndex: Int, val value: String) : ResultsUiEvent()
    data class ClearWeekMultiplierOverride(val weekIndex: Int) : ResultsUiEvent()
    data object ToggleResultsExpand : ResultsUiEvent()
    data class CopyResults(val results: List<Long>) : ResultsUiEvent()
}
