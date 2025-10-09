package org.aalbertini.ham.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.aalbertini.ham.applyRoundingRules
import org.aalbertini.ham.calculateWeeklyResults
import org.aalbertini.ham.filterNumericInput
import org.aalbertini.ham.repository.MovieResultRepository
import org.aalbertini.ham.ui.state.CalculatorUiEvent
import org.aalbertini.ham.ui.state.CalculatorUiState
import org.aalbertini.ham.ui.state.NotificationMessage
import org.aalbertini.ham.ui.state.NotificationType

/**
 * ViewModel for Calculator screen
 * Handles all business logic and state management
 */
class CalculatorViewModel(
    private val repository: MovieResultRepository = MovieResultRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()
    
    init {
        loadSavedMovieResults()
    }
    
    fun onEvent(event: CalculatorUiEvent) {
        when (event) {
            is CalculatorUiEvent.UpdateP1Input -> updateP1Input(event.value)
            is CalculatorUiEvent.UpdateP2Input -> updateP2Input(event.value)
            is CalculatorUiEvent.UpdateEditableTitle -> updateEditableTitle(event.title)
            is CalculatorUiEvent.RevertTitle -> revertTitle()
            is CalculatorUiEvent.SaveMovieResult -> saveMovieResult(event.title)
            is CalculatorUiEvent.LoadMovieResult -> loadMovieResult(event.movieResultId)
            is CalculatorUiEvent.UpdateMovieResult -> updateMovieResult(event.id, event.title, event.p1, event.p2)
            is CalculatorUiEvent.DeleteMovieResult -> deleteMovieResult(event.movieResultId)
            is CalculatorUiEvent.ClearAllMovieResults -> clearAllMovieResults()
            is CalculatorUiEvent.NewMovieResult -> newMovieResult()
            is CalculatorUiEvent.ToggleResultsExpand -> toggleResultsExpand()
            is CalculatorUiEvent.ToggleSavedExpand -> toggleSavedExpand()
            is CalculatorUiEvent.DismissNotification -> dismissNotification()
            is CalculatorUiEvent.CopyResults -> Unit // Handled in UI
        }
    }
    
    private fun updateP1Input(value: String) {
        val filtered = filterNumericInput(value)
        _uiState.update { it.copy(p1Input = filtered) }
        calculateResults()
    }
    
    private fun updateP2Input(value: String) {
        val filtered = filterNumericInput(value)
        _uiState.update { it.copy(p2Input = filtered) }
        calculateResults()
    }
    
    private fun updateEditableTitle(title: String) {
        _uiState.update { it.copy(editableTitle = title) }
    }
    
    private fun revertTitle() {
        _uiState.update { it.copy(editableTitle = it.originalTitle ?: "") }
    }
    
    private fun calculateResults() {
        val state = _uiState.value
        val p1 = state.p1Input.toDoubleOrNull()
        val p2 = state.p2Input.toDoubleOrNull()
        
        val p1Valid = p1 != null && 
                     p1 >= org.aalbertini.ham.CalculationConstants.Validation.COMMERCIAL_SCORE_MIN && 
                     p1 <= org.aalbertini.ham.CalculationConstants.Validation.COMMERCIAL_SCORE_MAX
        val p2Valid = p2 != null && 
                     p2 >= org.aalbertini.ham.CalculationConstants.Validation.SEATS_MIN && 
                     p2 <= org.aalbertini.ham.CalculationConstants.Validation.SEATS_MAX
        
        val results = if (p1Valid && p2Valid && p1 != null && p2 != null) {
            val rawResults = calculateWeeklyResults(p1, p2)
            applyRoundingRules(rawResults)
        } else {
            emptyList()
        }
        
        _uiState.update { it.copy(resultsWithRounded = results) }
    }
    
    private fun saveMovieResult(title: String) {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val p1 = state.p1Input.toDoubleOrNull()
                val p2 = state.p2Input.toDoubleOrNull()
                
                if (title.isBlank() || p1 == null || p2 == null) return@launch
                
                if (state.currentMovieResultId != null) {
                    // Update existing
                    repository.updateMovieResult(state.currentMovieResultId, title, p1, p2)
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
                    // Create new
                    val newMovieResult = repository.saveMovieResult(title, p1, p2)
                    _uiState.update {
                        it.copy(
                            currentMovieResultId = newMovieResult.id,
                            currentMovieResultTitle = newMovieResult.title,
                            editableTitle = newMovieResult.title,
                            originalTitle = newMovieResult.title,
                            notification = NotificationMessage(
                                "Movie result saved successfully",
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
        viewModelScope.launch {
            val movieResult = repository.getMovieResultById(movieResultId)
            if (movieResult != null) {
                _uiState.update {
                    it.copy(
                        p1Input = movieResult.commercialScore.toString(),
                        p2Input = movieResult.numberOfSeats.toString(),
                        currentMovieResultId = movieResult.id,
                        currentMovieResultTitle = movieResult.title,
                        editableTitle = movieResult.title,
                        originalTitle = movieResult.title,
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
    
    private fun updateMovieResult(id: String, title: String, p1: Double, p2: Double) {
        viewModelScope.launch {
            try {
                repository.updateMovieResult(id, title, p1, p2)
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
        _uiState.update {
            it.copy(
                p1Input = "",
                p2Input = "",
                currentMovieResultId = null,
                currentMovieResultTitle = null,
                editableTitle = "",
                originalTitle = null,
                resultsWithRounded = emptyList()
            )
        }
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
}
