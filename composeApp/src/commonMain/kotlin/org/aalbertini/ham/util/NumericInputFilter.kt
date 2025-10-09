package org.aalbertini.ham.util

/**
 * Filters user text input to a permissive positive decimal number format:
 * - digits only and a single decimal separator '.' or ',' (normalized to '.')
 * - minus sign is not allowed (values must be > 0)
 * - if the first kept character is '.', prefix a leading zero so parsing works (e.g. ".5" -> "0.5")
 */
fun String.filterNumericInput(): String {
    val normalized = this.replace(',', '.')
    val sb = StringBuilder()
    var dotSeen = false

    normalized.forEach { c ->
        when {
            c in '0'..'9' -> sb.append(c)
            c == '.' && !dotSeen -> {
                sb.append('.'); dotSeen = true
            }

            else -> Unit // skip anything else
        }
    }
    var out = sb.toString()
    if (out.startsWith(".")) out = "0$out"
    return out
}
/**
 * Removes thousands separator formatting from a numeric string
 * This allows users to edit formatted numbers
 */
fun String.unformatNumber(): String {
    return this.replace(",", "").replace(" ", "")
}

/**
 * Formats a numeric string with thousands separators if it's a valid integer
 * Returns the original string if it contains a decimal point or is invalid
 */
fun String.formatNumericString(): String {
    val cleaned = this.unformatNumber()
    val number = cleaned.toDoubleOrNull() ?: return this
    
    // If it has a decimal part, don't format (keep as-is for decimal editing)
    if (cleaned.contains('.')) {
        return this
    }
    
    // Format integer part with thousands separators
    val intValue = number.toLong()
    return intValue.toString().reversed().chunked(3).joinToString(",").reversed()
}

/**
 * Filters numeric input with max decimal places restriction
 * @param maxDecimalPlaces Maximum number of decimal places allowed (null = no limit)
 */
fun String.filterNumericInput(maxDecimalPlaces: Int? = null): String {
    val normalized = this.replace(',', '.')
    val sb = StringBuilder()
    var dotSeen = false
    var decimalCount = 0

    normalized.forEach { c ->
        when {
            c in '0'..'9' -> {
                // Only add digit if we haven't exceeded decimal place limit
                if (!dotSeen || maxDecimalPlaces == null || decimalCount < maxDecimalPlaces) {
                    sb.append(c)
                    if (dotSeen) decimalCount++
                }
            }
            c == '.' && !dotSeen -> {
                sb.append('.')
                dotSeen = true
            }
            else -> Unit // skip anything else
        }
    }
    var out = sb.toString()
    if (out.startsWith(".")) out = "0$out"
    return out
}

/**
 * Clamps a numeric string to a range, returns empty if invalid
 */
fun String.clampToRange(min: Double, max: Double): String {
    if (this.isEmpty()) return this
    val value = this.toDoubleOrNull() ?: return this
    return value.coerceIn(min, max).toString()
}
