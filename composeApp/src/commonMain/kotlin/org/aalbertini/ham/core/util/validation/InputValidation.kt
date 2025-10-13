package org.aalbertini.ham.core.util.validation

import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionConstants

/**
 * Generic input validation utilities.
 * 
 * Follows SOLID principles:
 * - Single Responsibility: Each function validates one specific thing
 * - Open/Closed: Extensible through composition
 * - Interface Segregation: Small, focused interfaces
 * 
 * Thread-safe and null-safe by design using immutable data.
 */

/**
 * Validation result container.
 * 
 * @param isValid Whether the validation passed
 * @param errorMessage Error message if validation failed
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
) {
    companion object {
        val Valid = ValidationResult(true)
        fun invalid(message: String) = ValidationResult(false, message)
    }
}

/**
 * Generic validator interface.
 */
fun interface Validator<T> {
    fun validate(value: T): ValidationResult
}

/**
 * Validates a numeric string within a range.
 * 
 * @param value Input value
 * @param minValue Minimum valid value (inclusive)
 * @param maxValue Maximum valid value (inclusive)
 * @param fieldName Field name for error messages
 * @return Validation result
 */
fun validateNumericRange(
    value: String,
    minValue: Double,
    maxValue: Double,
    fieldName: String = "Value"
): ValidationResult {
    if (value.isEmpty()) {
        return ValidationResult.Valid
    }
    
    val numericValue = value.toDoubleOrNull()
        ?: return ValidationResult.invalid("$fieldName must be a valid number")
    
    return when {
        numericValue < minValue -> 
            ValidationResult.invalid("$fieldName must be at least ${minValue.toLong()}")
        numericValue > maxValue -> 
            ValidationResult.invalid("$fieldName must be at most ${maxValue.toLong()}")
        else -> ValidationResult.Valid
    }
}

/**
 * Validates commercial score input.
 * 
 * Thread-safe and null-safe.
 */
fun validateCommercialScore(value: String): ValidationResult {
    return validateNumericRange(
        value = value,
        minValue = MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MIN,
        maxValue = MovieDistributionConstants.Validation.COMMERCIAL_SCORE_MAX,
        fieldName = "Commercial Score"
    )
}

/**
 * Validates number of screenings input.
 * 
 * Thread-safe and null-safe.
 */
fun validateScreenings(value: String): ValidationResult {
    return validateNumericRange(
        value = value,
        minValue = MovieDistributionConstants.Validation.SCREENINGS_MIN,
        maxValue = MovieDistributionConstants.Validation.SCREENINGS_MAX,
        fieldName = "Number of Screenings"
    )
}

/**
 * Validates that a string is not blank.
 */
fun validateNotBlank(value: String, fieldName: String = "Field"): ValidationResult {
    return if (value.isBlank()) {
        ValidationResult.invalid("$fieldName cannot be empty")
    } else {
        ValidationResult.Valid
    }
}

/**
 * Combines multiple validators with AND logic.
 * All validators must pass for the result to be valid.
 */
fun <T> combineValidators(vararg validators: Validator<T>): Validator<T> {
    return Validator { value ->
        validators.forEach { validator ->
            val result = validator.validate(value)
            if (!result.isValid) {
                return@Validator result
            }
        }
        ValidationResult.Valid
    }
}

/**
 * Creates a validator that checks if a value is in a list of valid values.
 */
fun <T> createInListValidator(
    validValues: List<T>,
    errorMessage: String = "Invalid value"
): Validator<T> {
    return Validator { value ->
        if (value in validValues) {
            ValidationResult.Valid
        } else {
            ValidationResult.invalid(errorMessage)
        }
    }
}

/**
 * Creates a range validator for numeric types.
 */
fun createRangeValidator(
    min: Double,
    max: Double,
    fieldName: String = "Value"
): Validator<String> {
    return Validator { value ->
        validateNumericRange(value, min, max, fieldName)
    }
}
