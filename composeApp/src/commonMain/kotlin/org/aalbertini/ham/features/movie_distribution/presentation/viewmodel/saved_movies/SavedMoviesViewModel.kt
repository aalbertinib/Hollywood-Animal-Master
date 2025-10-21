package org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.saved_movies

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
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ClearAllMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.DeleteMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.GetMovieResultByIdUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.GetMovieResultByTitleUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.LoadMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.SaveMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.UpdateMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.presentation.state.NotificationMessage
import org.aalbertini.ham.features.movie_distribution.presentation.state.NotificationType
import org.aalbertini.ham.features.movie_distribution.presentation.state.ParameterConflict
import org.aalbertini.ham.features.movie_distribution.presentation.state.saved_movies.SavedMoviesUiEvent
import org.aalbertini.ham.features.movie_distribution.presentation.state.saved_movies.SavedMoviesUiState

/**
 * ViewModel for Saved Movies section
 * Handles loading, saving, updating, and deleting movie results
 */
class SavedMoviesViewModel(
    private val loadMovieResultsUseCase: LoadMovieResultsUseCase,
    private val saveMovieResultUseCase: SaveMovieResultUseCase,
    private val updateMovieResultUseCase: UpdateMovieResultUseCase,
    private val deleteMovieResultUseCase: DeleteMovieResultUseCase,
    private val clearAllMovieResultsUseCase: ClearAllMovieResultsUseCase,
    private val getMovieResultByIdUseCase: GetMovieResultByIdUseCase,
    private val getMovieResultByTitleUseCase: GetMovieResultByTitleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedMoviesUiState())
    val uiState: StateFlow<SavedMoviesUiState> = _uiState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        SavedMoviesUiState()
    )

    // Callbacks for coordinating with other ViewModels
    var onMovieLoaded: ((String, String, Double, Double, Map<Int, Double>, Map<Int, Double>) -> Unit)? = null
    var onMovieCleared: (() -> Unit)? = null

    init {
        loadSavedMovieResults()
    }

    fun onEvent(event: SavedMoviesUiEvent) {
        when (event) {
            is SavedMoviesUiEvent.AutoSaveMovieResult -> {
                // AutoSaveMovieResult is handled through the coordinator
                // This event is kept for backward compatibility but does nothing
            }
            is SavedMoviesUiEvent.SaveMovieResult -> saveMovieResult(event.title)
            is SavedMoviesUiEvent.LoadMovieResult -> loadMovieResult(event.movieResultId)
            is SavedMoviesUiEvent.UpdateMovieResult -> updateMovieResult(
                event.id,
                event.title,
                event.commercialScore,
                event.availableScreenings
            )
            is SavedMoviesUiEvent.DeleteMovieResult -> deleteMovieResult(event.movieResultId)
            is SavedMoviesUiEvent.ClearAllMovieResults -> clearAllMovieResults()
            is SavedMoviesUiEvent.ToggleSavedExpand -> toggleSavedExpand()
            is SavedMoviesUiEvent.DismissNotification -> dismissNotification()
            is SavedMoviesUiEvent.OverwriteConflictingMovie -> overwriteConflictingMovie()
            is SavedMoviesUiEvent.KeepExistingMovie -> keepExistingMovie()
            is SavedMoviesUiEvent.DismissParameterConflict -> dismissParameterConflict()
        }
    }

    private fun saveMovieResult(title: String) {
        viewModelScope.launch {
            try {
                // Parameters will be provided through a callback or shared state
                // For now, this is a placeholder
                _uiState.update {
                    it.copy(
                        notification = NotificationMessage(
                            "Save functionality requires parameter coordination",
                            NotificationType.INFO
                        )
                    )
                }
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
     * Auto-saves movie result with provided parameters
     */
    fun autoSaveMovieResult(
        title: String,
        commercialScore: Double,
        availableScreenings: Double,
        availableScreeningsOverrides: Map<Int, Double>,
        weekMultiplierOverrides: Map<Int, Double>,
        currentMovieResultId: String?
    ) {
        viewModelScope.launch {
            try {
                if (title.isBlank()) return@launch

                // Validate inputs
                val commercialScoreValid = commercialScore >= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN &&
                        commercialScore <= MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX
                val availableScreeningsValid = availableScreenings >= MovieDistributionConstants.Validation.SCREENINGS_MIN &&
                        availableScreenings <= MovieDistributionConstants.Validation.SCREENINGS_MAX

                if (!commercialScoreValid || !availableScreeningsValid) return@launch

                // Check if a movie with this title already exists
                val existingMovie = getMovieResultByTitleUseCase(title)

                // Check if we're updating the current movie
                if (currentMovieResultId != null) {
                    // Get the current movie to check if title changed
                    val currentMovie = getMovieResultByIdUseCase(currentMovieResultId)
                    val titleChanged = currentMovie != null && !currentMovie.title.equals(title, ignoreCase = true)
                    
                    // Check if the title was changed to match a different existing movie
                    if (existingMovie != null && existingMovie.id != currentMovieResultId) {
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
                                        newScreenings = availableScreenings,
                                        newAvailableScreeningsOverrides = availableScreeningsOverrides,
                                        newWeekMultiplierOverrides = weekMultiplierOverrides
                                    )
                                )
                            }
                            return@launch
                        } else {
                            // Same parameters, switch to the existing movie
                            loadMovieResult(existingMovie.id)
                            _uiState.update {
                                it.copy(
                                    notification = NotificationMessage(
                                        "Switched to existing movie: ${existingMovie.title}",
                                        NotificationType.INFO
                                    )
                                )
                            }
                            return@launch
                        }
                    }

                    // If title changed to a new name, create a new save
                    if (titleChanged) {
                        val newMovieResult = saveMovieResultUseCase(
                            title,
                            commercialScore,
                            availableScreenings,
                            availableScreeningsOverrides,
                            weekMultiplierOverrides
                        )
                        
                        // Notify that the new movie was created and loaded
                        onMovieLoaded?.invoke(
                            newMovieResult.id,
                            newMovieResult.title,
                            newMovieResult.commercialScore,
                            newMovieResult.numberOfScreenings,
                            newMovieResult.availableScreeningsOverrides,
                            newMovieResult.weekMultiplierOverrides
                        )
                        
                        _uiState.update {
                            it.copy(
                                notification = NotificationMessage(
                                    "New movie created: $title",
                                    NotificationType.SUCCESS
                                )
                            )
                        }
                        loadSavedMovieResults()
                        return@launch
                    }

                    // Title hasn't changed - update existing movie
                    updateMovieResultUseCase(
                        currentMovieResultId,
                        title,
                        commercialScore,
                        availableScreenings,
                        availableScreeningsOverrides,
                        weekMultiplierOverrides
                    )
                    _uiState.update {
                        it.copy(
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
                    val parametersDifferent = existingMovie.commercialScore != commercialScore ||
                            existingMovie.numberOfScreenings != availableScreenings

                    if (parametersDifferent) {
                        // Show conflict dialog
                        _uiState.update {
                            it.copy(
                                parameterConflict = ParameterConflict(
                                    existingMovie = existingMovie,
                                    newCommercialScore = commercialScore,
                                    newScreenings = availableScreenings,
                                    newAvailableScreeningsOverrides = availableScreeningsOverrides,
                                    newWeekMultiplierOverrides = weekMultiplierOverrides
                                )
                            )
                        }
                        return@launch
                    } else {
                        // Same parameters, just load the existing movie
                        loadMovieResult(existingMovie.id)
                        _uiState.update {
                            it.copy(
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
                val newMovieResult = saveMovieResultUseCase(
                    title,
                    commercialScore,
                    availableScreenings,
                    availableScreeningsOverrides,
                    weekMultiplierOverrides
                )
                
                // Notify that movie was saved
                onMovieLoaded?.invoke(
                    newMovieResult.id,
                    newMovieResult.title,
                    newMovieResult.commercialScore,
                    newMovieResult.numberOfScreenings,
                    newMovieResult.availableScreeningsOverrides,
                    newMovieResult.weekMultiplierOverrides
                )
                
                _uiState.update {
                    it.copy(
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
                            "Failed to save movie result: ${e.message}",
                            NotificationType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun loadMovieResult(movieResultId: String) {
        viewModelScope.launch {
            val movieResult = getMovieResultByIdUseCase(movieResultId)
            if (movieResult != null) {
                // Notify other ViewModels to load this movie
                onMovieLoaded?.invoke(
                    movieResult.id,
                    movieResult.title,
                    movieResult.commercialScore,
                    movieResult.numberOfScreenings,
                    movieResult.availableScreeningsOverrides,
                    movieResult.weekMultiplierOverrides
                )
                
                _uiState.update {
                    it.copy(
                        notification = NotificationMessage(
                            "Movie result loaded: ${movieResult.title}",
                            NotificationType.INFO
                        )
                    )
                }
            }
        }
    }

    private fun updateMovieResult(
        id: String,
        title: String,
        commercialScore: Double,
        availableScreenings: Double
    ) {
        viewModelScope.launch {
            try {
                // Get current overrides from the movie
                val existingMovie = getMovieResultByIdUseCase(id)
                if (existingMovie == null) {
                    _uiState.update {
                        it.copy(
                            notification = NotificationMessage(
                                "Movie not found",
                                NotificationType.ERROR
                            )
                        )
                    }
                    return@launch
                }
                
                val availableScreeningsOverrides = existingMovie.availableScreeningsOverrides
                val weekMultiplierOverrides = existingMovie.weekMultiplierOverrides
                
                // Check if the title has changed
                if (title != existingMovie.title) {
                    // Title changed - check if a movie with the new title already exists
                    val movieWithNewTitle = getMovieResultByTitleUseCase(title)
                    
                    if (movieWithNewTitle != null) {
                        // A movie with the new title already exists
                        val parametersDifferent = movieWithNewTitle.commercialScore != commercialScore ||
                                movieWithNewTitle.numberOfScreenings != availableScreenings
                        
                        if (parametersDifferent) {
                            // Show conflict dialog
                            _uiState.update {
                                it.copy(
                                    parameterConflict = ParameterConflict(
                                        existingMovie = movieWithNewTitle,
                                        newCommercialScore = commercialScore,
                                        newScreenings = availableScreenings,
                                        newAvailableScreeningsOverrides = availableScreeningsOverrides,
                                        newWeekMultiplierOverrides = weekMultiplierOverrides
                                    )
                                )
                            }
                            return@launch
                        } else {
                            // Same parameters, switch to the existing movie
                            loadMovieResult(movieWithNewTitle.id)
                            _uiState.update {
                                it.copy(
                                    notification = NotificationMessage(
                                        "Switched to existing movie: ${movieWithNewTitle.title}",
                                        NotificationType.INFO
                                    )
                                )
                            }
                            return@launch
                        }
                    }
                    
                    // Create a new save with the new title
                    val newMovieResult = saveMovieResultUseCase(
                        title,
                        commercialScore,
                        availableScreenings,
                        availableScreeningsOverrides,
                        weekMultiplierOverrides
                    )
                    
                    // Notify that the new movie was created and loaded
                    onMovieLoaded?.invoke(
                        newMovieResult.id,
                        newMovieResult.title,
                        newMovieResult.commercialScore,
                        newMovieResult.numberOfScreenings,
                        newMovieResult.availableScreeningsOverrides,
                        newMovieResult.weekMultiplierOverrides
                    )
                    
                    loadSavedMovieResults()
                    _uiState.update {
                        it.copy(
                            notification = NotificationMessage(
                                "New movie created: $title",
                                NotificationType.SUCCESS
                            )
                        )
                    }
                } else {
                    // Title hasn't changed - update the existing movie
                    updateMovieResultUseCase(
                        id,
                        title,
                        commercialScore,
                        availableScreenings,
                        availableScreeningsOverrides,
                        weekMultiplierOverrides
                    )
                    
                    // Notify other ViewModels to update with the new parameters
                    onMovieLoaded?.invoke(
                        id,
                        title,
                        commercialScore,
                        availableScreenings,
                        availableScreeningsOverrides,
                        weekMultiplierOverrides
                    )
                    
                    loadSavedMovieResults()
                    _uiState.update {
                        it.copy(
                            notification = NotificationMessage(
                                "Movie result updated",
                                NotificationType.SUCCESS
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        notification = NotificationMessage(
                            "Failed to update movie result: ${e.message}",
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
                deleteMovieResultUseCase(movieResultId)
                loadSavedMovieResults()
                
                // Notify to clear current if we deleted it
                onMovieCleared?.invoke()
                
                _uiState.update {
                    it.copy(
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
                clearAllMovieResultsUseCase()
                loadSavedMovieResults()

                // Clear current movie result
                onMovieCleared?.invoke()
                
                _uiState.update {
                    it.copy(
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

    private fun loadSavedMovieResults() {
        viewModelScope.launch {
            val movieResults = loadMovieResultsUseCase()
            _uiState.update { it.copy(savedMovieResults = movieResults) }
        }
    }

    private fun toggleSavedExpand() {
        _uiState.update { it.copy(expandSaved = !it.expandSaved) }
    }

    private fun dismissNotification() {
        _uiState.update { it.copy(notification = null) }
    }

    private fun overwriteConflictingMovie() {
        viewModelScope.launch {
            try {
                val conflict = _uiState.value.parameterConflict ?: return@launch

                // Update the existing movie with new parameters
                updateMovieResultUseCase(
                    conflict.existingMovie.id,
                    conflict.existingMovie.title,
                    conflict.newCommercialScore,
                    conflict.newScreenings,
                    conflict.newAvailableScreeningsOverrides,
                    conflict.newWeekMultiplierOverrides
                )

                // Notify other ViewModels to update with the new parameters
                onMovieLoaded?.invoke(
                    conflict.existingMovie.id,
                    conflict.existingMovie.title,
                    conflict.newCommercialScore,
                    conflict.newScreenings,
                    conflict.newAvailableScreeningsOverrides,
                    conflict.newWeekMultiplierOverrides
                )

                _uiState.update {
                    it.copy(
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

    private fun keepExistingMovie() {
        val conflict = _uiState.value.parameterConflict ?: return
        
        // Load the existing movie
        loadMovieResult(conflict.existingMovie.id)
        
        _uiState.update {
            it.copy(
                parameterConflict = null,
                notification = NotificationMessage(
                    "Loaded existing movie: ${conflict.existingMovie.title}",
                    NotificationType.INFO
                )
            )
        }
    }

    private fun dismissParameterConflict() {
        _uiState.update { it.copy(parameterConflict = null) }
    }
}
