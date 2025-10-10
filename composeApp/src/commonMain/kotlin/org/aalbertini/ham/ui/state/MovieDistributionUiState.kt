package org.aalbertini.ham.ui.state

import org.aalbertini.ham.model.MovieResult

/**
 * UI State for the Calculator screen
 */
data class MovieDistributionUiState(
    val commercialScoreInput: String = "",
    val availableScreeningsInput: String = "",
    val availableScreeningsOverrides: Map<Int, Double> = emptyMap(), // Per-week availableScreenings overrides (week index -> availableScreenings value)
    val availableScreeningsOverrideInputs: Map<Int, String> = emptyMap(), // Per-week availableScreenings input strings for UI
    val currentMovieResultId: String? = null,
    val currentMovieResultTitle: String? = null,
    val editableTitle: String = "",
    val originalTitle: String? = null,
    val originalCommercialScore: String? = null,
    val originalAvailableScreenings: String? = null,
    val savedMovieResults: List<MovieResult> = emptyList(),
    val expandResults: Boolean = true,
    val expandSaved: Boolean = true,
    val resultsWithRounded: List<Long> = emptyList(),
    val isLoading: Boolean = false,
    val notification: NotificationMessage? = null,
    val parameterConflict: ParameterConflict? = null
)

/**
 * Represents a conflict when saving a movie with different parameters
 */
data class ParameterConflict(
    val existingMovie: MovieResult,
    val newCommercialScore: Double,
    val newScreenings: Double
)

/**
 * Notification message for user feedback
 */
data class NotificationMessage(
    val message: String,
    val type: NotificationType = NotificationType.INFO
)

enum class NotificationType {
    SUCCESS,
    ERROR,
    INFO
}
