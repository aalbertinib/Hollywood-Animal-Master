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
            is MovieDistributionUiEvent.UpdateP1Input -> updateP1Input(event.value)
            is MovieDistributionUiEvent.UpdateP2Input -> updateP2Input(event.value)
            is MovieDistributionUiEvent.UpdateEditableTitle -> updateEditableTitle(event.title)
            is MovieDistributionUiEvent.RevertTitle -> revertTitle()
            is MovieDistributionUiEvent.SaveMovieResult -> saveMovieResult(event.title)
            is MovieDistributionUiEvent.LoadMovieResult -> loadMovieResult(event.movieResultId)
            is MovieDistributionUiEvent.UpdateMovieResult -> updateMovieResult(event.id, event.title, event.p1, event.p2)
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

    private fun updateP1Input(value: String) {
        val filtered = value.filterNumericInput()
        _uiState.update { it.copy(p1Input = filtered) }
        calculateResults()
    }

    private fun updateP2Input(value: String) {
        val filtered = value.filterNumericInput()
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
                     p1 >= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN &&
                     p1 <= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
        val p2Valid = p2 != null &&
                     p2 >= MovieDistributionConstants.Validation.SEATS_MIN &&
                     p2 <= MovieDistributionConstants.Validation.SEATS_MAX

        val results = if (p1Valid && p2Valid) {
            val rawResults = MovieDistributionCalculator.calculateWeeklyResults(p1, p2)
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

    /**
     * Attempts to auto-save the current movie result
     * If a movie with the same title exists with different parameters, shows conflict dialog
     */
    private fun autoSaveMovieResult() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val title = state.editableTitle.ifBlank { return@launch }
                val p1 = state.p1Input.toDoubleOrNull() ?: return@launch
                val p2 = state.p2Input.toDoubleOrNull() ?: return@launch

                // Validate inputs
                val p1Valid = p1 >= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN &&
                             p1 <= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
                val p2Valid = p2 >= MovieDistributionConstants.Validation.SEATS_MIN &&
                             p2 <= MovieDistributionConstants.Validation.SEATS_MAX

                if (!p1Valid || !p2Valid) return@launch

                // Check if a movie with this title already exists
                val existingMovie = repository.getMovieResultByTitle(title)

                // Check if we're updating the current movie
                if (state.currentMovieResultId != null) {
                    // Check if the title was changed to match a different existing movie
                    if (existingMovie != null && existingMovie.id != state.currentMovieResultId) {
                        // Title conflicts with a different movie
                        val parametersDifferent = existingMovie.commercialScore != p1 ||
                                                 existingMovie.numberOfSeats != p2

                        if (parametersDifferent) {
                            // Show conflict dialog
                            _uiState.update {
                                it.copy(
                                    parameterConflict = ParameterConflict(
                                        existingMovie = existingMovie,
                                        newCommercialScore = p1,
                                        newSeats = p2
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

                    // Update the current movie (no title conflict)
                    repository.updateMovieResult(state.currentMovieResultId, title, p1, p2)
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
                    loadSavedMovieResults()
                    return@launch
                }

                // Creating a new movie - check for conflicts
                if (existingMovie != null) {
                    // Check if parameters are different
                    val parametersDifferent = existingMovie.commercialScore != p1 ||
                                             existingMovie.numberOfSeats != p2

                    if (parametersDifferent) {
                        // Show conflict dialog
                        _uiState.update {
                            it.copy(
                                parameterConflict = ParameterConflict(
                                    existingMovie = existingMovie,
                                    newCommercialScore = p1,
                                    newSeats = p2
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
                val newMovieResult = repository.saveMovieResult(title, p1, p2)
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
                    conflict.newSeats
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

        _uiState.update {
            it.copy(
                p1Input = conflict.existingMovie.commercialScore.toString(),
                p2Input = conflict.existingMovie.numberOfSeats.toString(),
                currentMovieResultId = conflict.existingMovie.id,
                currentMovieResultTitle = conflict.existingMovie.title,
                editableTitle = conflict.existingMovie.title,
                originalTitle = conflict.existingMovie.title,
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