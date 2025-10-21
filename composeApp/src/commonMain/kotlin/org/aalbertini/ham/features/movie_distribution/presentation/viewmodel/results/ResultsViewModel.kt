package org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants
import org.aalbertini.ham.features.movie_distribution.domain.use_case.CalculateWeeklyResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.UpdateMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.presentation.state.results.ResultsUiEvent
import org.aalbertini.ham.features.movie_distribution.presentation.state.results.ResultsUiState

/**
 * ViewModel for Results section
 * Handles calculation results and overrides
 */
class ResultsViewModel(
    private val calculateWeeklyResultsUseCase: CalculateWeeklyResultsUseCase = CalculateWeeklyResultsUseCase(),
    private val updateMovieResultUseCase: UpdateMovieResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultsUiState())
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        ResultsUiState()
    )

    // Callback for when overrides change and need to trigger recalculation
    var onOverridesChanged: (() -> Unit)? = null

    fun onEvent(event: ResultsUiEvent) {
        when (event) {
            is ResultsUiEvent.UpdateAvailableScreeningsOverride -> updateAvailableScreeningsOverride(
                event.weekIndex,
                event.value
            )
            is ResultsUiEvent.ClearAvailableScreeningsOverride -> clearAvailableScreeningsOverride(event.weekIndex)
            is ResultsUiEvent.UpdateWeekMultiplierOverride -> updateWeekMultiplierOverride(
                event.weekIndex,
                event.value
            )
            is ResultsUiEvent.ClearWeekMultiplierOverride -> clearWeekMultiplierOverride(event.weekIndex)
            is ResultsUiEvent.ToggleResultsExpand -> toggleResultsExpand()
            is ResultsUiEvent.CopyResults -> Unit // Handled in UI
        }
    }

    /**
     * Calculates results based on current parameters
     */
    fun calculateResults(
        commercialScore: Double?,
        availableScreenings: Double?
    ) {
        val commercialScoreValid = commercialScore != null &&
                commercialScore >= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN &&
                commercialScore <= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
        val availableScreeningsValid = availableScreenings != null &&
                availableScreenings >= MovieDistributionConstants.Validation.SCREENINGS_MIN &&
                availableScreenings <= MovieDistributionConstants.Validation.SCREENINGS_MAX

        val results = if (commercialScoreValid && availableScreeningsValid) {
            calculateWeeklyResultsUseCase(
                commercialScore = commercialScore!!,
                availableScreenings = availableScreenings!!,
                availableScreeningsOverrides = _uiState.value.availableScreeningsOverrides,
                weekMultiplierOverrides = _uiState.value.weekMultiplierOverrides
            )
        } else {
            emptyList()
        }

        _uiState.update { it.copy(resultsWithRounded = results) }
    }

    private fun updateAvailableScreeningsOverride(weekIndex: Int, value: String) {
        val state = _uiState.value
        
        // Update input string immediately
        val newInputs = state.availableScreeningsOverrideInputs.toMutableMap()
        if (value.isEmpty()) {
            newInputs.remove(weekIndex)
        } else {
            newInputs[weekIndex] = value
        }
        
        // Validate and update override value for calculation
        val newOverrides = state.availableScreeningsOverrides.toMutableMap()
        val overrideValue = value.toDoubleOrNull()
        
        // Only update overrides map if value is valid
        if (overrideValue != null && overrideValue >= 0.0) {
            newOverrides[weekIndex] = overrideValue
        } else if (value.isEmpty()) {
            newOverrides.remove(weekIndex)
        } else {
            // Invalid value - remove from overrides but keep in inputs for UI display
            newOverrides.remove(weekIndex)
        }
        
        // Update state and recalculate only if overrides changed
        val overridesChanged = state.availableScreeningsOverrides != newOverrides
        
        _uiState.update { 
            it.copy(
                availableScreeningsOverrideInputs = newInputs,
                availableScreeningsOverrides = newOverrides
            ) 
        }
        
        // Trigger recalculation if valid overrides changed
        if (overridesChanged) {
            onOverridesChanged?.invoke()
            autoSaveOverrides()
        }
    }

    private fun clearAvailableScreeningsOverride(weekIndex: Int) {
        val state = _uiState.value
        val newOverrides = state.availableScreeningsOverrides - weekIndex
        
        _uiState.update { 
            it.copy(
                availableScreeningsOverrideInputs = it.availableScreeningsOverrideInputs - weekIndex,
                availableScreeningsOverrides = newOverrides
            ) 
        }
        
        onOverridesChanged?.invoke()
        autoSaveOverrides()
    }

    private fun updateWeekMultiplierOverride(weekIndex: Int, value: String) {
        val state = _uiState.value
        
        // Filter to allow only positive integers (no negative values, only reductions)
        val filtered = value.filter { it.isDigit() }
        
        // Apply range restriction if value is complete (not just typing)
        val finalValue = if (filtered.isNotEmpty()) {
            val intValue = filtered.toIntOrNull()
            when {
                intValue == null -> filtered
                intValue > 100 -> "100" // Max 100% reduction (multiplier becomes 0)
                else -> filtered
            }
        } else {
            filtered
        }
        
        // Update input string with filtered value
        val newInputs = state.weekMultiplierOverrideInputs.toMutableMap()
        if (finalValue.isEmpty()) {
            newInputs.remove(weekIndex)
        } else {
            newInputs[weekIndex] = finalValue
        }
        
        // Validate and update override value for calculation
        // Convert reduction percentage to absolute multiplier
        // e.g., if previous week is 1.0 and user enters "20", then multiplier = 1.0 * (1 - 20/100) = 0.8
        val newOverrides = state.weekMultiplierOverrides.toMutableMap()
        val reductionPercentage = finalValue.toIntOrNull()
        
        // Accept range 0% .. 100% (integers only, representing reduction)
        if (reductionPercentage != null && reductionPercentage >= 0 && reductionPercentage <= 100) {
            // Get previous week's default multiplier
            val previousWeekDefault = if (weekIndex > 0) {
                MovieDistributionConstants.Multipliers.DEFAULT_WEEK_MULTIPLIERS.getOrElse(weekIndex - 1) { 1.0 }
            } else {
                1.0 // Week 1 has no previous week
            }
            // Calculate absolute multiplier based on reduction percentage from previous week
            val multiplierValue = previousWeekDefault * (1.0 - (reductionPercentage / 100.0))
            newOverrides[weekIndex] = multiplierValue
        } else if (finalValue.isEmpty()) {
            newOverrides.remove(weekIndex)
        } else {
            // Invalid value - remove from overrides but keep in inputs for UI display
            newOverrides.remove(weekIndex)
        }
        
        // Update state and recalculate only if overrides changed
        val overridesChanged = state.weekMultiplierOverrides != newOverrides
        
        _uiState.update { 
            it.copy(
                weekMultiplierOverrideInputs = newInputs,
                weekMultiplierOverrides = newOverrides
            ) 
        }
        
        // Trigger recalculation if valid overrides changed
        if (overridesChanged) {
            onOverridesChanged?.invoke()
            autoSaveOverrides()
        }
    }

    private fun clearWeekMultiplierOverride(weekIndex: Int) {
        val state = _uiState.value
        val newOverrides = state.weekMultiplierOverrides - weekIndex
        
        _uiState.update { 
            it.copy(
                weekMultiplierOverrideInputs = it.weekMultiplierOverrideInputs - weekIndex,
                weekMultiplierOverrides = newOverrides
            ) 
        }
        
        onOverridesChanged?.invoke()
        autoSaveOverrides()
    }

    private fun toggleResultsExpand() {
        _uiState.update { it.copy(expandResults = !it.expandResults) }
    }

    /**
     * Auto-saves overrides if there's a current movie loaded
     */
    private fun autoSaveOverrides() {
        val state = _uiState.value
        if (state.currentMovieResultId != null) {
            // Trigger auto-save through callback
            // The actual save will be handled by SavedMoviesViewModel
        }
    }

    /**
     * Loads overrides from a movie result
     */
    fun loadOverrides(
        availableScreeningsOverrides: Map<Int, Double>,
        weekMultiplierOverrides: Map<Int, Double>,
        currentMovieResultId: String?
    ) {
        // Convert availableScreeningsOverrides to input strings
        val screeningsOverrideInputs = availableScreeningsOverrides.mapValues { entry ->
            val value = entry.value
            // Normalize: remove trailing zeros for whole numbers
            if (value == value.toLong().toDouble()) {
                value.toLong().toString()
            } else {
                value.toString()
            }
        }
        
        // Convert weekMultiplierOverrides to input strings (absolute multiplier to reduction percentage)
        val multiplierOverrideInputs = weekMultiplierOverrides.mapValues { (weekIdx, absoluteMultiplier) ->
            // Get previous week's default multiplier
            val previousWeekDefault = if (weekIdx > 0) {
                MovieDistributionConstants.Multipliers.DEFAULT_WEEK_MULTIPLIERS.getOrElse(weekIdx - 1) { 1.0 }
            } else {
                1.0
            }
            // Calculate reduction percentage from previous week
            // reduction = (1 - (current/previous)) * 100
            val reductionPercentage = if (previousWeekDefault > 0.0) {
                ((1.0 - (absoluteMultiplier / previousWeekDefault)) * 100).toInt()
            } else {
                0
            }
            reductionPercentage.toString()
        }
        
        _uiState.update {
            it.copy(
                availableScreeningsOverrides = availableScreeningsOverrides,
                availableScreeningsOverrideInputs = screeningsOverrideInputs,
                weekMultiplierOverrides = weekMultiplierOverrides,
                weekMultiplierOverrideInputs = multiplierOverrideInputs,
                currentMovieResultId = currentMovieResultId
            )
        }
    }

    /**
     * Clears all overrides
     */
    fun clearOverrides() {
        _uiState.update {
            it.copy(
                availableScreeningsOverrides = emptyMap(),
                availableScreeningsOverrideInputs = emptyMap(),
                weekMultiplierOverrides = emptyMap(),
                weekMultiplierOverrideInputs = emptyMap()
            )
        }
    }

    /**
     * Updates the current movie ID
     */
    fun updateCurrentMovieId(id: String?) {
        _uiState.update { it.copy(currentMovieResultId = id) }
    }
}
