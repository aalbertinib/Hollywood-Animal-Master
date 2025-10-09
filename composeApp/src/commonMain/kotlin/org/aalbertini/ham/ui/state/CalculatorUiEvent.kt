package org.aalbertini.ham.ui.state

/**
 * UI Events for the Calculator screen
 */
sealed class CalculatorUiEvent {
    data class UpdateP1Input(val value: String) : CalculatorUiEvent()
    data class UpdateP2Input(val value: String) : CalculatorUiEvent()
    data class UpdateEditableTitle(val title: String) : CalculatorUiEvent()
    data object RevertTitle : CalculatorUiEvent()
    data class SaveMovieResult(val title: String) : CalculatorUiEvent()
    data class LoadMovieResult(val movieResultId: String) : CalculatorUiEvent()
    data class UpdateMovieResult(val id: String, val title: String, val p1: Double, val p2: Double) : CalculatorUiEvent()
    data class DeleteMovieResult(val movieResultId: String) : CalculatorUiEvent()
    data object ClearAllMovieResults : CalculatorUiEvent()
    data object NewMovieResult : CalculatorUiEvent()
    data object ToggleResultsExpand : CalculatorUiEvent()
    data object ToggleSavedExpand : CalculatorUiEvent()
    data object DismissNotification : CalculatorUiEvent()
    data class CopyResults(val results: List<Long>) : CalculatorUiEvent()
}
