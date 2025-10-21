package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.card

/**
 * Shared validation helpers for week card inputs to keep UI components atomic
 * and ensure a single source of truth for validation rules.
 */
internal object WeekCardValidation {
    private const val EPSILON = 0.0001

    fun isValidScreeningsInput(input: String, maxAllowed: Double): Boolean {
        if (input.isEmpty()) return true
        val num = input.toDoubleOrNull() ?: return false
        return num >= 0.0 && num <= maxAllowed + EPSILON
    }

    fun isValidMultiplierInput(input: String): Boolean {
        if (input.isEmpty()) return true
        // Validate reduction percentage input (0 to 100)
        val num = input.toIntOrNull() ?: return false
        return num >= 0 && num <= 100
    }
}
