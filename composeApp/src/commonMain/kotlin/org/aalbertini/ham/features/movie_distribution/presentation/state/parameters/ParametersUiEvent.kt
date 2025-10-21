package org.aalbertini.ham.features.movie_distribution.presentation.state.parameters

/**
 * UI Events for the Parameters section
 */
sealed class ParametersUiEvent {
    data class UpdateCommercialScoreInput(val value: String) : ParametersUiEvent()
    data class UpdateAvailableScreeningsInput(val value: String) : ParametersUiEvent()
    data class UpdateEditableTitle(val title: String) : ParametersUiEvent()
    data object RevertTitle : ParametersUiEvent()
    data object RevertCommercialScore : ParametersUiEvent()
    data object RevertAvailableScreenings : ParametersUiEvent()
    data object NewMovieResult : ParametersUiEvent()
}
