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