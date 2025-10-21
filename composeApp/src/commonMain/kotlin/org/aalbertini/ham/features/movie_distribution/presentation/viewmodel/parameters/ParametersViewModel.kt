package org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.parameters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.aalbertini.ham.core.util.input.filterIntegerInput
import org.aalbertini.ham.core.util.input.filterNumericInput
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.aalbertini.ham.features.movie_distribution.presentation.state.parameters.ParametersUiEvent
import org.aalbertini.ham.features.movie_distribution.presentation.state.parameters.ParametersUiState

/**
 * ViewModel for Parameters section
 * Handles commercial score, available screenings, and title inputs
 */
class ParametersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ParametersUiState())
    val uiState: StateFlow<ParametersUiState> = _uiState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        ParametersUiState()
    )

    fun onEvent(event: ParametersUiEvent) {
        when (event) {
            is ParametersUiEvent.UpdateCommercialScoreInput -> updateCommercialScoreInput(event.value)
            is ParametersUiEvent.UpdateAvailableScreeningsInput -> updateAvailableScreeningsInput(event.value)
            is ParametersUiEvent.UpdateEditableTitle -> updateEditableTitle(event.title)
            is ParametersUiEvent.RevertTitle -> revertTitle()
            is ParametersUiEvent.RevertCommercialScore -> revertCommercialScore()
            is ParametersUiEvent.RevertAvailableScreenings -> revertAvailableScreenings()
            is ParametersUiEvent.NewMovieResult -> newMovieResult()
        }
    }

    private fun updateCommercialScoreInput(value: String) {
        // Limit to 1 decimal place for commercial score
        val filtered = value.filterNumericInput(maxDecimalPlaces = 1)
        
        // Apply range restriction if value is complete (not just typing)
        val finalValue = if (filtered.isNotEmpty() && !filtered.endsWith(".")) {
            val numValue = filtered.toDoubleOrNull()
            if (numValue != null && numValue > MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX) {
                MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX.toString()
            } else {
                filtered
            }
        } else {
            filtered
        }
        
        _uiState.update { it.copy(commercialScoreInput = finalValue) }
    }

    private fun updateAvailableScreeningsInput(value: String) {
        val filtered = value.filterIntegerInput()
        
        // Apply range restriction if value is complete (not just typing)
        val finalValue = if (filtered.isNotEmpty() && !filtered.endsWith(".")) {
            val numValue = filtered.toDoubleOrNull()
            if (numValue != null && numValue > MovieDistributionConstants.Validation.SCREENINGS_MAX) {
                MovieDistributionConstants.Validation.SCREENINGS_MAX.toLong().toString()
            } else {
                filtered
            }
        } else {
            filtered
        }
        
        _uiState.update { it.copy(availableScreeningsInput = finalValue) }
    }

    private fun updateEditableTitle(title: String) {
        _uiState.update { it.copy(editableTitle = title) }
    }

    private fun revertTitle() {
        _uiState.update { it.copy(editableTitle = it.originalTitle ?: "") }
    }

    private fun revertCommercialScore() {
        _uiState.update { it.copy(commercialScoreInput = it.originalCommercialScore ?: "") }
    }

    private fun revertAvailableScreenings() {
        _uiState.update { it.copy(availableScreeningsInput = it.originalAvailableScreenings ?: "") }
    }

    private fun newMovieResult() {
        val defaultCommercialScore = MovieDistributionConstants.Defaults.COMMERCIAL_SCORE.toString()
        val defaultAvailableScreenings = MovieDistributionConstants.Defaults.AVAILABLE_SCREENINGS.toLong().toString()
        
        _uiState.update {
            it.copy(
                commercialScoreInput = defaultCommercialScore,
                availableScreeningsInput = defaultAvailableScreenings,
                currentMovieResultId = null,
                currentMovieResultTitle = null,
                editableTitle = "",
                originalTitle = null,
                originalCommercialScore = null,
                originalAvailableScreenings = null
            )
        }
    }

    /**
     * Loads movie parameters from a movie result
     */
    fun loadMovieParameters(
        id: String,
        title: String,
        commercialScore: Double,
        numberOfScreenings: Double
    ) {
        _uiState.update {
            it.copy(
                commercialScoreInput = commercialScore.toString(),
                availableScreeningsInput = numberOfScreenings.toString(),
                currentMovieResultId = id,
                currentMovieResultTitle = title,
                editableTitle = title,
                originalTitle = title,
                originalCommercialScore = commercialScore.toString(),
                originalAvailableScreenings = numberOfScreenings.toString()
            )
        }
    }

    /**
     * Clears current movie context
     */
    fun clearCurrentMovie() {
        _uiState.update {
            it.copy(
                currentMovieResultId = null,
                currentMovieResultTitle = null,
                originalTitle = null,
                originalCommercialScore = null,
                originalAvailableScreenings = null
            )
        }
    }

    /**
     * Updates the current movie ID (used after save)
     */
    fun updateCurrentMovieId(id: String, title: String) {
        _uiState.update {
            it.copy(
                currentMovieResultId = id,
                currentMovieResultTitle = title,
                editableTitle = title,
                originalTitle = title
            )
        }
    }
}
