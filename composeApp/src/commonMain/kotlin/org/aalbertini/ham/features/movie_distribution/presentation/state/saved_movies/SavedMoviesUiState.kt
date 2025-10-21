package org.aalbertini.ham.features.movie_distribution.presentation.state.saved_movies

import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.presentation.state.NotificationMessage
import org.aalbertini.ham.features.movie_distribution.presentation.state.ParameterConflict

/**
 * UI State for the Saved Movies section
 */
data class SavedMoviesUiState(
    val savedMovieResults: List<MovieResult> = emptyList(),
    val expandSaved: Boolean = true,
    val notification: NotificationMessage? = null,
    val parameterConflict: ParameterConflict? = null
)
