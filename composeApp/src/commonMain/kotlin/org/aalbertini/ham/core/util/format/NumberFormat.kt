package org.aalbertini.ham.core.util.format

import kotlin.math.pow
import kotlin.math.round

/**
 * Multiplatform-safe fixed-decimal formatter for Double.
 * Ensures the returned string always has [decimals] fractional digits.
 */
fun Double.toFixed(decimals: Int): String {
    require(decimals >= 0) { "decimals must be >= 0" }
    val factor = 10.0.pow(decimals)
    val rounded = round(this * factor) / factor
    val raw = rounded.toString()
    if (decimals == 0) return raw.substringBefore('.')
    val parts = raw.split('.')
    val integer = parts[0]
    val fraction = (parts.getOrNull(1) ?: "0").padEnd(decimals, '0').take(decimals)
    return "$integer.$fraction"
}
