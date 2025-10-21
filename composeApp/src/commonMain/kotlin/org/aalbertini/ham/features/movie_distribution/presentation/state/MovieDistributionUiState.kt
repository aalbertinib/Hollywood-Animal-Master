package org.aalbertini.ham.features.movie_distribution.presentation.state

import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult

/**
 * Represents the saveable state of a movie for change detection
 */
data class MovieSaveableState(
    val title: String,
    val commercialScore: String,
    val availableScreenings: String,
    val availableScreeningsOverrides: Map<Int, Double>,
    val weekMultiplierOverrides: Map<Int, Double>
)

/**
 * UI State for the Calculator screen
 */
data class MovieDistributionUiState(
    val commercialScoreInput: String = "",
    val availableScreeningsInput: String = "",
    val availableScreeningsOverrides: Map<Int, Double> = emptyMap(), // Per-week availableScreenings overrides (week index -> availableScreenings value)
    val availableScreeningsOverrideInputs: Map<Int, String> = emptyMap(), // Per-week availableScreenings input strings for UI
    val weekMultiplierOverrides: Map<Int, Double> = emptyMap(), // Per-week multiplier overrides (week index -> multiplier value)
    val weekMultiplierOverrideInputs: Map<Int, String> = emptyMap(), // Per-week multiplier input strings for UI
    val currentMovieResultId: String? = null,
    val currentMovieResultTitle: String? = null,
    val editableTitle: String = "",
    val originalTitle: String? = null,
    val originalCommercialScore: String? = null,
    val originalAvailableScreenings: String? = null,
    val originalAvailableScreeningsOverrides: Map<Int, Double> = emptyMap(), // Original overrides for change tracking
    val originalWeekMultiplierOverrides: Map<Int, Double> = emptyMap(), // Original multiplier overrides for change tracking
    val savedMovieResults: List<MovieResult> = emptyList(),
    val expandResults: Boolean = true,
    val expandSaved: Boolean = true,
    val resultsWithRounded: List<Long> = emptyList(),
    val isLoading: Boolean = false,
    val notification: NotificationMessage? = null,
    val parameterConflict: ParameterConflict? = null
) {
    /**
     * Gets the current saveable state
     */
    fun getCurrentSaveableState(): MovieSaveableState = MovieSaveableState(
        title = editableTitle,
        commercialScore = commercialScoreInput,
        availableScreenings = availableScreeningsInput,
        availableScreeningsOverrides = availableScreeningsOverrides,
        weekMultiplierOverrides = weekMultiplierOverrides
    )
    
    /**
     * Gets the original saveable state (when movie was loaded)
     */
    fun getOriginalSaveableState(): MovieSaveableState? {
        return if (originalTitle != null && originalCommercialScore != null && originalAvailableScreenings != null) {
            MovieSaveableState(
                title = originalTitle,
                commercialScore = originalCommercialScore,
                availableScreenings = originalAvailableScreenings,
                availableScreeningsOverrides = originalAvailableScreeningsOverrides,
                weekMultiplierOverrides = originalWeekMultiplierOverrides
            )
        } else {
            null
        }
    }
    
    /**
     * Checks if there are unsaved changes
     * Note: Compares numeric values for availableScreenings to ignore formatting differences
     */
    fun hasUnsavedChanges(): Boolean {
        val original = getOriginalSaveableState() ?: return false
        val current = getCurrentSaveableState()
        
        // Compare numeric values for availableScreenings (ignore formatting)
        val currentScreenings = current.availableScreenings.toDoubleOrNull()
        val originalScreenings = original.availableScreenings.toDoubleOrNull()
        val screeningsChanged = currentScreenings != originalScreenings
        
        // Check if any field changed
        return current.title != original.title ||
               current.commercialScore != original.commercialScore ||
               screeningsChanged ||
               current.availableScreeningsOverrides != original.availableScreeningsOverrides ||
               current.weekMultiplierOverrides != original.weekMultiplierOverrides
    }
    
    /**
     * Checks if current state is up to date with a saved movie
     * Returns true if:
     * - A movie is loaded (currentMovieResultId is not null)
     * - Current title matches a saved movie title
     * - All parameters match the saved movie (commercialScore, availableScreenings, overrides)
     * Note: Compares numeric values for availableScreenings to ignore formatting differences
     */
    fun isUpToDate(): Boolean {
        // Must have a loaded movie
        if (currentMovieResultId == null) return false
        
        // Check if current state matches original saved state
        val original = getOriginalSaveableState() ?: return false
        val current = getCurrentSaveableState()
        
        // Compare numeric values for availableScreenings (ignore formatting)
        val currentScreenings = current.availableScreenings.toDoubleOrNull()
        val originalScreenings = original.availableScreenings.toDoubleOrNull()
        val screeningsMatch = currentScreenings == originalScreenings
        
        // Compare all fields
        return current.title == original.title &&
               current.commercialScore == original.commercialScore &&
               screeningsMatch &&
               current.availableScreeningsOverrides == original.availableScreeningsOverrides &&
               current.weekMultiplierOverrides == original.weekMultiplierOverrides
    }
    
    /**
     * Checks if there are pending changes (opposite of isUpToDate)
     */
    fun hasPendingChanges(): Boolean {
        return currentMovieResultId != null && !isUpToDate()
    }
}

/**
 * Represents a conflict when saving a movie with different parameters
 */
data class ParameterConflict(
    val existingMovie: MovieResult,
    val newCommercialScore: Double,
    val newScreenings: Double,
    val newAvailableScreeningsOverrides: Map<Int, Double> = emptyMap(),
    val newWeekMultiplierOverrides: Map<Int, Double> = emptyMap()
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
