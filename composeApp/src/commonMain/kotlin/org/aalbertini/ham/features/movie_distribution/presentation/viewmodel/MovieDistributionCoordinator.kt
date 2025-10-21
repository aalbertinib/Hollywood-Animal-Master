package org.aalbertini.ham.features.movie_distribution.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.aalbertini.ham.features.movie_distribution.data.data_source.MovieResultDataSource
import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ClearAllMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.DeleteMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.GetMovieResultByIdUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.GetMovieResultByTitleUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.LoadMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.SaveMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.UpdateMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.presentation.state.MovieDistributionUiState
import org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.parameters.ParametersViewModel
import org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.results.ResultsViewModel
import org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.saved_movies.SavedMoviesViewModel

/**
 * Coordinator ViewModel that manages communication between section ViewModels
 * Follows CLEAN architecture by coordinating presentation layer components
 */
class MovieDistributionCoordinator : ViewModel() {

    // Initialize repository and use cases
    private val dataSource = MovieResultDataSource()
    private val repository = MovieResultRepository(dataSource)
    
    private val loadMovieResultsUseCase = LoadMovieResultsUseCase(repository)
    private val saveMovieResultUseCase = SaveMovieResultUseCase(repository)
    private val updateMovieResultUseCase = UpdateMovieResultUseCase(repository)
    private val deleteMovieResultUseCase = DeleteMovieResultUseCase(repository)
    private val clearAllMovieResultsUseCase = ClearAllMovieResultsUseCase(repository)
    private val getMovieResultByIdUseCase = GetMovieResultByIdUseCase(repository)
    private val getMovieResultByTitleUseCase = GetMovieResultByTitleUseCase(repository)

    // Section ViewModels
    val parametersViewModel = ParametersViewModel()
    val resultsViewModel = ResultsViewModel(
        updateMovieResultUseCase = updateMovieResultUseCase
    )
    val savedMoviesViewModel = SavedMoviesViewModel(
        loadMovieResultsUseCase = loadMovieResultsUseCase,
        saveMovieResultUseCase = saveMovieResultUseCase,
        updateMovieResultUseCase = updateMovieResultUseCase,
        deleteMovieResultUseCase = deleteMovieResultUseCase,
        clearAllMovieResultsUseCase = clearAllMovieResultsUseCase,
        getMovieResultByIdUseCase = getMovieResultByIdUseCase,
        getMovieResultByTitleUseCase = getMovieResultByTitleUseCase
    )

    // Combined UI state for backward compatibility
    val uiState: StateFlow<MovieDistributionUiState> = combine(
        parametersViewModel.uiState,
        resultsViewModel.uiState,
        savedMoviesViewModel.uiState
    ) { parametersState, resultsState, savedMoviesState ->
        MovieDistributionUiState(
            commercialScoreInput = parametersState.commercialScoreInput,
            availableScreeningsInput = parametersState.availableScreeningsInput,
            availableScreeningsOverrides = resultsState.availableScreeningsOverrides,
            availableScreeningsOverrideInputs = resultsState.availableScreeningsOverrideInputs,
            weekMultiplierOverrides = resultsState.weekMultiplierOverrides,
            weekMultiplierOverrideInputs = resultsState.weekMultiplierOverrideInputs,
            currentMovieResultId = parametersState.currentMovieResultId,
            currentMovieResultTitle = parametersState.currentMovieResultTitle,
            editableTitle = parametersState.editableTitle,
            originalTitle = parametersState.originalTitle,
            originalCommercialScore = parametersState.originalCommercialScore,
            originalAvailableScreenings = parametersState.originalAvailableScreenings,
            savedMovieResults = savedMoviesState.savedMovieResults,
            expandResults = resultsState.expandResults,
            expandSaved = savedMoviesState.expandSaved,
            resultsWithRounded = resultsState.resultsWithRounded,
            notification = savedMoviesState.notification,
            parameterConflict = savedMoviesState.parameterConflict
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        MovieDistributionUiState()
    )

    init {
        setupCoordination()
    }

    /**
     * Sets up coordination between ViewModels
     */
    private fun setupCoordination() {
        // When parameters change, recalculate results
        viewModelScope.launch {
            parametersViewModel.uiState.collect { parametersState ->
                val commercialScore = parametersState.commercialScoreInput.toDoubleOrNull()
                val availableScreenings = parametersState.availableScreeningsInput.toDoubleOrNull()
                resultsViewModel.calculateResults(commercialScore, availableScreenings)
            }
        }

        // When results overrides change, recalculate
        resultsViewModel.onOverridesChanged = {
            viewModelScope.launch {
                val parametersState = parametersViewModel.uiState.value
                val commercialScore = parametersState.commercialScoreInput.toDoubleOrNull()
                val availableScreenings = parametersState.availableScreeningsInput.toDoubleOrNull()
                resultsViewModel.calculateResults(commercialScore, availableScreenings)
            }
        }

        // When a movie is loaded from saved movies
        savedMoviesViewModel.onMovieLoaded = { id: String, title: String, commercialScore: Double, numberOfScreenings: Double, screeningsOverrides: Map<Int, Double>, multiplierOverrides: Map<Int, Double> ->
            parametersViewModel.loadMovieParameters(id, title, commercialScore, numberOfScreenings)
            resultsViewModel.loadOverrides(screeningsOverrides, multiplierOverrides, id)
            
            // Trigger calculation with loaded data
            viewModelScope.launch {
                resultsViewModel.calculateResults(commercialScore, numberOfScreenings)
            }
        }

        // When current movie is cleared
        savedMoviesViewModel.onMovieCleared = {
            val currentId = parametersViewModel.uiState.value.currentMovieResultId
            if (currentId != null) {
                parametersViewModel.clearCurrentMovie()
                resultsViewModel.updateCurrentMovieId(null)
            }
        }
    }

    /**
     * Handles auto-save triggered from parameters or results sections
     */
    fun autoSaveCurrentMovie() {
        viewModelScope.launch {
            val parametersState = parametersViewModel.uiState.value
            val resultsState = resultsViewModel.uiState.value
            
            val title = parametersState.editableTitle
            val commercialScore = parametersState.commercialScoreInput.toDoubleOrNull() ?: return@launch
            val availableScreenings = parametersState.availableScreeningsInput.toDoubleOrNull() ?: return@launch
            
            savedMoviesViewModel.autoSaveMovieResult(
                title = title,
                commercialScore = commercialScore,
                availableScreenings = availableScreenings,
                availableScreeningsOverrides = resultsState.availableScreeningsOverrides,
                weekMultiplierOverrides = resultsState.weekMultiplierOverrides,
                currentMovieResultId = parametersState.currentMovieResultId
            )
        }
    }
}
