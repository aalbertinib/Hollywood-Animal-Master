package org.aalbertini.ham.features.movie_distribution.presentation.components.section.results.week.card

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WeekCardValidationTest {

    @Test
    fun screenings_empty_is_valid() {
        assertTrue(WeekCardValidation.isValidScreeningsInput("", 100.0))
    }

    @Test
    fun screenings_negative_is_invalid() {
        assertFalse(WeekCardValidation.isValidScreeningsInput("-1", 100.0))
    }

    @Test
    fun screenings_over_max_is_invalid() {
        assertFalse(WeekCardValidation.isValidScreeningsInput("100.1", 100.0))
    }

    @Test
    fun screenings_at_max_is_valid() {
        assertTrue(WeekCardValidation.isValidScreeningsInput("100", 100.0))
        assertTrue(WeekCardValidation.isValidScreeningsInput("100.0", 100.0))
    }

    @Test
    fun screenings_non_numeric_is_invalid() {
        assertFalse(WeekCardValidation.isValidScreeningsInput("abc", 100.0))
    }

    @Test
    fun multiplier_empty_is_valid() {
        assertTrue(WeekCardValidation.isValidMultiplierInput(""))
    }

    @Test
    fun multiplier_bounds() {
        assertTrue(WeekCardValidation.isValidMultiplierInput("0"))
        assertTrue(WeekCardValidation.isValidMultiplierInput("10"))
        assertFalse(WeekCardValidation.isValidMultiplierInput("10.0001"))
        assertFalse(WeekCardValidation.isValidMultiplierInput("-0.1"))
    }

    @Test
    fun multiplier_non_numeric_is_invalid() {
        assertFalse(WeekCardValidation.isValidMultiplierInput("abc"))
    }
}
