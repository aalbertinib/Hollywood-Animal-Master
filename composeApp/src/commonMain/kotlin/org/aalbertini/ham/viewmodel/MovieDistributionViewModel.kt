package org.aalbertini.ham.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.aalbertini.ham.MovieDistributionCalculator
import org.aalbertini.ham.MovieDistributionConstants
import org.aalbertini.ham.repository.MovieResultRepository
import org.aalbertini.ham.ui.state.MovieDistributionUiEvent
import org.aalbertini.ham.ui.state.MovieDistributionUiState
import org.aalbertini.ham.ui.state.NotificationMessage
import org.aalbertini.ham.ui.state.NotificationType
import org.aalbertini.ham.ui.state.ParameterConflict
import org.aalbertini.ham.util.filterIntegerInput
import org.aalbertini.ham.util.filterNumericInput

/**
 * ViewModel for Calculator screen
 * Handles all business logic and state management
 */
class MovieDistributionViewModel(
    private val repository: MovieResultRepository = MovieResultRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDistributionUiState())
    val uiState: StateFlow<MovieDistributionUiState> = _uiState.asStateFlow().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5_000L), MovieDistributionUiState())

    init {
        loadSavedMovieResults()
    }

    fun onEvent(event: MovieDistributionUiEvent) {
        when (event) {
            is MovieDistributionUiEvent.UpdateCommercialScoreInput -> updateCommercialScoreInput(event.value)
            is MovieDistributionUiEvent.UpdateAvailableScreeningsInput -> updateAvailableScreeningsInput(event.value)
            is MovieDistributionUiEvent.UpdateAvailableScreeningsOverride -> updateAvailableScreeningsOverride(event.weekIndex, event.value)
            is MovieDistributionUiEvent.ClearAvailableScreeningsOverride -> clearAvailableScreeningsOverride(event.weekIndex)
            is MovieDistributionUiEvent.UpdateEditableTitle -> updateEditableTitle(event.title)
            is MovieDistributionUiEvent.RevertTitle -> revertTitle()
            is MovieDistributionUiEvent.RevertCommercialScore -> revertCommercialScore()
            is MovieDistributionUiEvent.RevertAvailableScreenings -> revertAvailableScreenings()
            is MovieDistributionUiEvent.SaveMovieResult -> saveMovieResult(event.title)
            is MovieDistributionUiEvent.LoadMovieResult -> loadMovieResult(event.movieResultId)
            is MovieDistributionUiEvent.UpdateMovieResult -> updateMovieResult(event.id, event.title, event.commercialScore, event.availableScreenings)
            is MovieDistributionUiEvent.DeleteMovieResult -> deleteMovieResult(event.movieResultId)
            is MovieDistributionUiEvent.ClearAllMovieResults -> clearAllMovieResults()
            is MovieDistributionUiEvent.NewMovieResult -> newMovieResult()
            is MovieDistributionUiEvent.ToggleResultsExpand -> toggleResultsExpand()
            is MovieDistributionUiEvent.ToggleSavedExpand -> toggleSavedExpand()
            is MovieDistributionUiEvent.DismissNotification -> dismissNotification()
            is MovieDistributionUiEvent.AutoSaveMovieResult -> autoSaveMovieResult()
            is MovieDistributionUiEvent.DismissParameterConflict -> dismissParameterConflict()
            is MovieDistributionUiEvent.KeepExistingMovie -> keepExistingMovie()
            is MovieDistributionUiEvent.OverwriteConflictingMovie -> overwriteConflictingMovie()
            is MovieDistributionUiEvent.CopyResults -> Unit // Handled in UI
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
        calculateResults()
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
        calculateResults()
    }

    private fun updateAvailableScreeningsOverride(weekIndex: Int, value: String) {
        val state = _uiState.value
        val availableScreenings = state.availableScreeningsInput.toDoubleOrNull() ?: 0.0
        
        // Update input string immediately (no filtering here - done in UI)
        val newInputs = state.availableScreeningsOverrideInputs.toMutableMap()
        if (value.isEmpty()) {
            newInputs.remove(weekIndex)
        } else {
            newInputs[weekIndex] = value
        }
        
        // Validate and update override value for calculation
        val newOverrides = state.availableScreeningsOverrides.toMutableMap()
        val overrideValue = value.toDoubleOrNull()
        
        // Only update overrides map if value is valid (with epsilon for floating point precision)
        if (overrideValue != null && overrideValue >= 0.0 && overrideValue <= availableScreenings + 0.0001) {
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
        
        // Only recalculate if valid overrides changed (not on every keystroke)
        if (overridesChanged) {
            calculateResults()
            
            // Auto-save if there's a current movie loaded
            // Note: currentMovieResultId is temporarily set to null during movie loading
            // to prevent saving pending/invalid overrides from the previous movie
            if (state.currentMovieResultId != null) {
                viewModelScope.launch {
                    try {
                        val commercialScore = state.commercialScoreInput.toDoubleOrNull()
                        val availableScreeningsValue = state.availableScreeningsInput.toDoubleOrNull()
                        val title = state.currentMovieResultTitle
                        
                        if (commercialScore != null && availableScreeningsValue != null && title != null) {
                            repository.updateMovieResult(
                                state.currentMovieResultId,
                                title,
                                commercialScore,
                                availableScreeningsValue,
                                newOverrides
                            )
                        }
                    } catch (e: Exception) {
                        // Silent fail for auto-save
                    }
                }
            }
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
        calculateResults()
        
        // Auto-save if there's a current movie loaded
        // Note: currentMovieResultId is temporarily set to null during movie loading
        // to prevent saving pending/invalid overrides from the previous movie
        if (state.currentMovieResultId != null) {
            viewModelScope.launch {
                try {
                    val commercialScore = state.commercialScoreInput.toDoubleOrNull()
                    val availableScreenings = state.availableScreeningsInput.toDoubleOrNull()
                    val title = state.currentMovieResultTitle
                    
                    if (commercialScore != null && availableScreenings != null && title != null) {
                        repository.updateMovieResult(
                            state.currentMovieResultId,
                            title,
                            commercialScore,
                            availableScreenings,
                            newOverrides
                        )
                    }
                } catch (e: Exception) {
                    // Silent fail for auto-save
                }
            }
        }
    }

    private fun updateEditableTitle(title: String) {
        _uiState.update { it.copy(editableTitle = title) }
    }

    private fun revertTitle() {
        _uiState.update { it.copy(editableTitle = it.originalTitle ?: "") }
    }
    private fun revertCommercialScore() {
        _uiState.update { it.copy(commercialScoreInput = it.originalCommercialScore ?: "") }
        calculateResults()
    }

    private fun revertAvailableScreenings() {
        _uiState.update { it.copy(availableScreeningsInput = it.originalAvailableScreenings ?: "") }
        calculateResults()
    }

    private fun calculateResults() {
        val state = _uiState.value
        val commercialScore = state.commercialScoreInput.toDoubleOrNull()
        val availableScreenings = state.availableScreeningsInput.toDoubleOrNull()

        val commercialScoreValid = commercialScore != null &&
                     commercialScore >= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN &&
                     commercialScore <= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
        val availableScreeningsValid = availableScreenings != null &&
                     availableScreenings >= MovieDistributionConstants.Validation.SCREENINGS_MIN &&
                     availableScreenings <= MovieDistributionConstants.Validation.SCREENINGS_MAX

        val results = if (commercialScoreValid && availableScreeningsValid) {
            val rawResults = if (state.availableScreeningsOverrides.isNotEmpty()) {
                MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(commercialScore, availableScreenings, state.availableScreeningsOverrides)
            } else {
                MovieDistributionCalculator.calculateWeeklyResults(commercialScore, availableScreenings)
            }
            MovieDistributionCalculator.applyRoundingRules(rawResults)
        } else {
            emptyList()
        }

        _uiState.update { it.copy(resultsWithRounded = results) }
    }

    private fun saveMovieResult(title: String) {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val commercialScore = state.commercialScoreInput.toDoubleOrNull()
                val availableScreenings = state.availableScreeningsInput.toDoubleOrNull()

                if (title.isBlank() || commercialScore == null || availableScreenings == null) return@launch

                // Check if title has changed from the original loaded movie
                val titleChanged = state.currentMovieResultId != null && 
                                  state.originalTitle != null && 
                                  title != state.originalTitle

                if (state.currentMovieResultId != null && !titleChanged) {
                    // Update existing movie (same title)
                    repository.updateMovieResult(state.currentMovieResultId, title, commercialScore, availableScreenings, state.availableScreeningsOverrides)
                    _uiState.update {
                        it.copy(
                            currentMovieResultTitle = title,
                            editableTitle = title,
                            originalTitle = title,
                            notification = NotificationMessage(
                                "Movie result updated successfully",
                                NotificationType.SUCCESS
                            )
                        )
                    }
                } else {
                    // Create new movie (no existing movie or title changed - "Save As" behavior)
                    val newMovieResult = repository.saveMovieResult(title, commercialScore, availableScreenings, state.availableScreeningsOverrides)
                    _uiState.update {
                        it.copy(
                            currentMovieResultId = newMovieResult.id,
                            currentMovieResultTitle = newMovieResult.title,
                            editableTitle = newMovieResult.title,
                            originalTitle = newMovieResult.title,
                            notification = NotificationMessage(
                                if (titleChanged) "New movie created with different title" else "Movie result saved successfully",
                                NotificationType.SUCCESS
                            )
                        )
                    }
                }

                loadSavedMovieResults()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        notification = NotificationMessage(
                            "Failed to save movie result",
                            NotificationType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun loadMovieResult(movieResultId: String) {
        // First, clear any pending override inputs to prevent them from being saved
        _uiState.update {
            it.copy(
                availableScreeningsOverrideInputs = emptyMap()
            )
        }
        
        viewModelScope.launch {
            val movieResult = repository.getMovieResultById(movieResultId)
            if (movieResult != null) {
                // Convert availableScreeningsOverrides to input strings
                val overrideInputs = movieResult.availableScreeningsOverrides.mapValues { entry ->
                    val value = entry.value
                    // Normalize: remove trailing zeros for whole numbers
                    if (value == value.toLong().toDouble()) {
                        value.toLong().toString()
                    } else {
                        value.toString()
                    }
                }
                
                _uiState.update {
                    it.copy(
                        commercialScoreInput = movieResult.commercialScore.toString(),
                        availableScreeningsInput = movieResult.numberOfScreenings.toString(),
                        availableScreeningsOverrides = movieResult.availableScreeningsOverrides,
                        availableScreeningsOverrideInputs = overrideInputs,
                        currentMovieResultId = movieResult.id,
                        currentMovieResultTitle = movieResult.title,
                        editableTitle = movieResult.title,
                        originalTitle = movieResult.title,
                        originalCommercialScore = movieResult.commercialScore.toString(),
                        originalAvailableScreenings = movieResult.numberOfScreenings.toString(),
                        expandResults = true,
                        notification = NotificationMessage(
                            "Movie result loaded: ${movieResult.title}",
                            NotificationType.INFO
                        )
                    )
                }
                calculateResults()
            }
        }
    }

    private fun updateMovieResult(id: String, title: String, commercialScore: Double, availableScreenings: Double) {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                repository.updateMovieResult(id, title, commercialScore, availableScreenings, state.availableScreeningsOverrides)
                loadSavedMovieResults()
                _uiState.update {
                    it.copy(
                        notification = NotificationMessage(
                            "Movie result updated",
                            NotificationType.SUCCESS
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        notification = NotificationMessage(
                            "Failed to update movie result",
                            NotificationType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun deleteMovieResult(movieResultId: String) {
        viewModelScope.launch {
            try {
                repository.deleteMovieResult(movieResultId)
                loadSavedMovieResults()

                // Clear current if we deleted it
                val shouldClearCurrent = _uiState.value.currentMovieResultId == movieResultId
                _uiState.update {
                    it.copy(
                        currentMovieResultId = if (shouldClearCurrent) null else it.currentMovieResultId,
                        currentMovieResultTitle = if (shouldClearCurrent) null else it.currentMovieResultTitle,
                        notification = NotificationMessage(
                            "Movie result deleted",
                            NotificationType.SUCCESS
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        notification = NotificationMessage(
                            "Failed to delete movie result",
                            NotificationType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun clearAllMovieResults() {
        viewModelScope.launch {
            try {
                repository.clearAllMovieResults()
                loadSavedMovieResults()

                // Clear current movie result
                _uiState.update {
                    it.copy(
                        currentMovieResultId = null,
                        currentMovieResultTitle = null,
                        notification = NotificationMessage(
                            "All movie results cleared",
                            NotificationType.SUCCESS
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        notification = NotificationMessage(
                            "Failed to clear movie results",
                            NotificationType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun newMovieResult() {
        val defaultCommercialScore = MovieDistributionConstants.Defaults.COMMERCIAL_SCORE.toString()
        val defaultAvailableScreenings = MovieDistributionConstants.Defaults.AVAILABLE_SCREENINGS.toLong().toString()
        
        _uiState.update {
            it.copy(
                commercialScoreInput = defaultCommercialScore,
                availableScreeningsInput = defaultAvailableScreenings,
                availableScreeningsOverrides = emptyMap(),
                availableScreeningsOverrideInputs = emptyMap(),
                currentMovieResultId = null,
                currentMovieResultTitle = null,
                editableTitle = "",
                originalTitle = null,
                originalCommercialScore = null,
                originalAvailableScreenings = null,
                resultsWithRounded = emptyList(),
                expandResults = true
            )
        }
        // Trigger calculation with default values
        calculateResults()
    }

    private fun loadSavedMovieResults() {
        viewModelScope.launch {
            val movieResults = repository.loadMovieResults()
            _uiState.update { it.copy(savedMovieResults = movieResults) }
        }
    }

    private fun toggleResultsExpand() {
        _uiState.update { it.copy(expandResults = !it.expandResults) }
    }

    private fun toggleSavedExpand() {
        _uiState.update { it.copy(expandSaved = !it.expandSaved) }
    }

    private fun dismissNotification() {
        _uiState.update { it.copy(notification = null) }
    }

    /**
     * Attempts to auto-save the current movie result
     * If a movie with the same title exists with different parameters, shows conflict dialog
     */
    private fun autoSaveMovieResult() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val title = state.editableTitle.ifBlank { return@launch }
                val commercialScore = state.commercialScoreInput.toDoubleOrNull() ?: return@launch
                val availableScreenings = state.availableScreeningsInput.toDoubleOrNull() ?: return@launch

                // Validate inputs
                val commercialScoreValid = commercialScore >= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN &&
                             commercialScore <= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
                val availableScreeningsValid = availableScreenings >= MovieDistributionConstants.Validation.SCREENINGS_MIN &&
                             availableScreenings <= MovieDistributionConstants.Validation.SCREENINGS_MAX

                if (!commercialScoreValid || !availableScreeningsValid) return@launch

                // Check if a movie with this title already exists
                val existingMovie = repository.getMovieResultByTitle(title)

                // Check if we're updating the current movie
                if (state.currentMovieResultId != null) {
                    // Check if the title was changed to match a different existing movie
                    if (existingMovie != null && existingMovie.id != state.currentMovieResultId) {
                        // Title conflicts with a different movie
                        val parametersDifferent = existingMovie.commercialScore != commercialScore ||
                                                 existingMovie.numberOfScreenings != availableScreenings

                        if (parametersDifferent) {
                            // Show conflict dialog
                            _uiState.update {
                                it.copy(
                                    parameterConflict = ParameterConflict(
                                        existingMovie = existingMovie,
                                        newCommercialScore = commercialScore,
                                        newScreenings = availableScreenings
                                    )
                                )
                            }
                            return@launch
                        } else {
                            // Same parameters, switch to the existing movie
                            _uiState.update {
                                it.copy(
                                    currentMovieResultId = existingMovie.id,
                                    currentMovieResultTitle = existingMovie.title,
                                    editableTitle = existingMovie.title,
                                    originalTitle = existingMovie.title,
                                    notification = NotificationMessage(
                                        "Switched to existing movie: ${existingMovie.title}",
                                        NotificationType.INFO
                                    )
                                )
                            }
                            return@launch
                        }
                    }

                    // Check if title changed - if so, create new movie ("Save As" behavior)
                    val titleChanged = state.originalTitle != null && title != state.originalTitle
                    
                    if (titleChanged) {
                        // Title changed - create new movie
                        val newMovieResult = repository.saveMovieResult(title, commercialScore, availableScreenings, state.availableScreeningsOverrides)
                        _uiState.update {
                            it.copy(
                                currentMovieResultId = newMovieResult.id,
                                currentMovieResultTitle = newMovieResult.title,
                                editableTitle = newMovieResult.title,
                                originalTitle = newMovieResult.title,
                                notification = NotificationMessage(
                                    "New movie created with different title",
                                    NotificationType.SUCCESS
                                )
                            )
                        }
                    } else {
                        // Same title - update existing movie
                        repository.updateMovieResult(state.currentMovieResultId, title, commercialScore, availableScreenings, state.availableScreeningsOverrides)
                        _uiState.update {
                            it.copy(
                                currentMovieResultTitle = title,
                                editableTitle = title,
                                originalTitle = title,
                                notification = NotificationMessage(
                                    "Movie updated successfully",
                                    NotificationType.SUCCESS
                                )
                            )
                        }
                    }
                    loadSavedMovieResults()
                    return@launch
                }

                // Creating a new movie - check for conflicts
                if (existingMovie != null) {
                    // Check if parameters are different
                    val parametersDifferent = existingMovie.commercialScore != commercialScore ||
                                             existingMovie.numberOfScreenings != availableScreenings

                    if (parametersDifferent) {
                        // Show conflict dialog
                        _uiState.update {
                            it.copy(
                                parameterConflict = ParameterConflict(
                                    existingMovie = existingMovie,
                                    newCommercialScore = commercialScore,
                                    newScreenings = availableScreenings
                                )
                            )
                        }
                        return@launch
                    } else {
                        // Same parameters, just load the existing movie
                        _uiState.update {
                            it.copy(
                                currentMovieResultId = existingMovie.id,
                                currentMovieResultTitle = existingMovie.title,
                                editableTitle = existingMovie.title,
                                originalTitle = existingMovie.title,
                                notification = NotificationMessage(
                                    "Movie already saved",
                                    NotificationType.INFO
                                )
                            )
                        }
                        return@launch
                    }
                }

                // No conflict, create new movie
                val newMovieResult = repository.saveMovieResult(title, commercialScore, availableScreenings, state.availableScreeningsOverrides)
                _uiState.update {
                    it.copy(
                        currentMovieResultId = newMovieResult.id,
                        currentMovieResultTitle = newMovieResult.title,
                        editableTitle = newMovieResult.title,
                        originalTitle = newMovieResult.title,
                        notification = NotificationMessage(
                            "Movie saved successfully",
                            NotificationType.SUCCESS
                        )
                    )
                }
                loadSavedMovieResults()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        notification = NotificationMessage(
                            "Failed to save movie result",
                            NotificationType.ERROR
                        )
                    )
                }
            }
        }
    }

    /**
     * Overwrites the existing movie with new parameters
     * If we were editing a different movie, that movie is deleted
     */
    private fun overwriteConflictingMovie() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val conflict = state.parameterConflict ?: return@launch

                // Update the existing movie with new parameters
                repository.updateMovieResult(
                    conflict.existingMovie.id,
                    conflict.existingMovie.title,
                    conflict.newCommercialScore,
                    conflict.newScreenings,
                    state.availableScreeningsOverrides
                )

                // If we were editing a different movie, delete it (we're effectively replacing it)
                if (state.currentMovieResultId != null && state.currentMovieResultId != conflict.existingMovie.id) {
                    repository.deleteMovieResult(state.currentMovieResultId)
                }

                _uiState.update {
                    it.copy(
                        currentMovieResultId = conflict.existingMovie.id,
                        currentMovieResultTitle = conflict.existingMovie.title,
                        editableTitle = conflict.existingMovie.title,
                        originalTitle = conflict.existingMovie.title,
                        parameterConflict = null,
                        notification = NotificationMessage(
                            "Movie updated successfully",
                            NotificationType.SUCCESS
                        )
                    )
                }
                loadSavedMovieResults()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        parameterConflict = null,
                        notification = NotificationMessage(
                            "Failed to update movie",
                            NotificationType.ERROR
                        )
                    )
                }
            }
        }
    }

    /**
     * Keeps the existing movie and loads it
     */
    private fun keepExistingMovie() {
        val conflict = _uiState.value.parameterConflict ?: return
        
        // First, clear any pending override state and temporarily null the movie ID to prevent auto-save
        _uiState.update {
            it.copy(
                availableScreeningsOverrideInputs = emptyMap(),
                availableScreeningsOverrides = emptyMap(),
                currentMovieResultId = null
            )
        }

        // Convert availableScreeningsOverrides to input strings (normalize to integers)
        val overrideInputs = conflict.existingMovie.availableScreeningsOverrides.mapValues { entry ->
            val value = entry.value
            // Normalize: remove trailing zeros for whole numbers
            if (value == value.toLong().toDouble()) {
                value.toLong().toString()
            } else {
                value.toString()
            }
        }

        _uiState.update {
            it.copy(
                commercialScoreInput = conflict.existingMovie.commercialScore.toString(),
                availableScreeningsInput = conflict.existingMovie.numberOfScreenings.toString(),
                availableScreeningsOverrides = conflict.existingMovie.availableScreeningsOverrides,
                availableScreeningsOverrideInputs = overrideInputs,
                currentMovieResultId = conflict.existingMovie.id,
                currentMovieResultTitle = conflict.existingMovie.title,
                editableTitle = conflict.existingMovie.title,
                originalTitle = conflict.existingMovie.title,
                originalCommercialScore = conflict.existingMovie.commercialScore.toString(),
                originalAvailableScreenings = conflict.existingMovie.numberOfScreenings.toString(),
                expandResults = true,
                parameterConflict = null,
                notification = NotificationMessage(
                    "Loaded existing movie: ${conflict.existingMovie.title}",
                    NotificationType.INFO
                )
            )
        }
        calculateResults()
    }

    /**
     * Dismisses the parameter conflict dialog without action
     */
    private fun dismissParameterConflict() {
        _uiState.update { it.copy(parameterConflict = null) }
    }
}








