package org.aalbertini.ham.features.movie_distribution.presentation.state.results

/**
 * UI State for the Results section
 */
data class ResultsUiState(
    val resultsWithRounded: List<Long> = emptyList(),
    val availableScreeningsOverrides: Map<Int, Double> = emptyMap(),
    val availableScreeningsOverrideInputs: Map<Int, String> = emptyMap(),
    val weekMultiplierOverrides: Map<Int, Double> = emptyMap(),
    val weekMultiplierOverrideInputs: Map<Int, String> = emptyMap(),
    val expandResults: Boolean = true,
    val currentMovieResultId: String? = null
)
