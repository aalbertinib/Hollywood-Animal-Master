package org.aalbertini.ham.features.movie_distribution.presentation.state.parameters

/**
 * UI State for the Parameters section
 */
data class ParametersUiState(
    val commercialScoreInput: String = "",
    val availableScreeningsInput: String = "",
    val editableTitle: String = "",
    val originalTitle: String? = null,
    val originalCommercialScore: String? = null,
    val originalAvailableScreenings: String? = null,
    val currentMovieResultId: String? = null,
    val currentMovieResultTitle: String? = null
) {
    /**
     * Checks if there are unsaved changes
     */
    fun hasUnsavedChanges(): Boolean {
        if (originalTitle == null || originalCommercialScore == null || originalAvailableScreenings == null) {
            return false
        }
        
        // Compare numeric values for availableScreenings (ignore formatting)
        val currentScreenings = availableScreeningsInput.toDoubleOrNull()
        val originalScreeningsValue = originalAvailableScreenings.toDoubleOrNull()
        val screeningsChanged = currentScreenings != originalScreeningsValue
        
        return editableTitle != originalTitle ||
               commercialScoreInput != originalCommercialScore ||
               screeningsChanged
    }
}
