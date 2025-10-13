package org.aalbertini.ham.core.util.formatting

import kotlin.math.pow

/**
 * Generic number formatting utilities.
 * 
 * Follows SOLID principles:
 * - Single Responsibility: Each function handles one type of formatting
 * - Open/Closed: Extensible through composition
 * - Dependency Inversion: Depends on abstractions (format strategies)
 * 
 * Thread-safe and null-safe by design using immutable operations.
 */

/**
 * Format strategy interface for customizable formatting.
 */
fun interface NumberFormatter {
    fun format(value: Number): String
}

/**
 * Formats a number with thousand separators.
 * 
 * Thread-safe and null-safe.
 * 
 * @param value Number to format
 * @param separator Thousand separator (default: comma)
 * @return Formatted string
 */
fun formatNumberWithSeparators(value: Long, separator: String = ","): String {
    val absValue = kotlin.math.abs(value)
    val formattedAbs = absValue.toString().reversed().chunked(3).joinToString(separator).reversed()
    return if (value < 0) "-$formattedAbs" else formattedAbs
}

/**
 * Formats a double as an integer with thousand separators.
 * 
 * Thread-safe and null-safe.
 */
fun formatDoubleAsInteger(value: Double, separator: String = ","): String {
    return formatNumberWithSeparators(value.toLong(), separator)
}

/**
 * Removes formatting from a number string (removes separators).
 * 
 * Thread-safe and null-safe.
 * 
 * @param value Formatted number string
 * @return Unformatted number string
 */
fun removeNumberFormatting(value: String): String {
    return value.replace(",", "").replace(" ", "").trim()
}

/**
 * Formats a decimal number to a specified number of decimal places.
 * 
 * Thread-safe and null-safe.
 * 
 * @param value Number to format
 * @param decimalPlaces Number of decimal places
 * @return Formatted string
 */
fun formatDecimal(value: Double, decimalPlaces: Int): String {
    val multiplier = 10.0.pow(decimalPlaces.toDouble())
    val rounded = kotlin.math.round(value * multiplier) / multiplier
    
    // Convert to string and ensure proper decimal places
    val stringValue = rounded.toString()
    val parts = stringValue.split('.')
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1] else ""
    
    return if (decimalPlaces == 0) {
        integerPart
    } else {
        val paddedDecimal = decimalPart.padEnd(decimalPlaces, '0').take(decimalPlaces)
        "$integerPart.$paddedDecimal"
    }
}

/**
 * Normalizes a number string by removing trailing zeros and decimal point if not needed.
 * 
 * Thread-safe and null-safe.
 * 
 * @param value Number string
 * @return Normalized string
 */
fun normalizeNumberString(value: String): String? {
    val num = value.toDoubleOrNull() ?: return null
    return if (num == num.toLong().toDouble()) {
        num.toLong().toString()
    } else {
        // Remove trailing zeros after decimal point
        value.trimEnd('0').trimEnd('.')
    }
}

/**
 * Default number formatter using comma separators.
 */
object DefaultNumberFormatter : NumberFormatter {
    override fun format(value: Number): String {
        return formatNumberWithSeparators(value.toLong())
    }
}

/**
 * Percentage formatter.
 */
object PercentageFormatter : NumberFormatter {
    override fun format(value: Number): String {
        return "${formatDecimal(value.toDouble(), 1)}%"
    }
}

/**
 * Currency formatter (generic, no currency symbol).
 */
object CurrencyFormatter : NumberFormatter {
    override fun format(value: Number): String {
        return formatNumberWithSeparators(value.toLong())
    }
}

/**
 * Extension function to format any Number using a formatter.
 */
fun Number.format(formatter: NumberFormatter = DefaultNumberFormatter): String {
    return formatter.format(this)
}
