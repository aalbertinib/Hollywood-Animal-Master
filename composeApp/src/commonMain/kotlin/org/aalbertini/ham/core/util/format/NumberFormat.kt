package org.aalbertini.ham.core.util.format

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

/** `12345` → `12,345`, `-1200` → `-1,200`. */
fun Long.grouped(): String {
    val digits = abs(this).toString().reversed().chunked(3).joinToString(",").reversed()
    return if (this < 0) "-$digits" else digits
}

fun Int.grouped(): String = toLong().grouped()

/** A difference: `+2,400`, `−1,200` (minus sign, not hyphen), `0`. */
fun Long.signedGrouped(): String = when {
    this > 0 -> "+" + grouped()
    this < 0 -> "−" + (-this).grouped()
    else -> "0"
}

/**
 * Plain decimal text with at most [maxDecimals] digits and no trailing zeros:
 * `3200.0` → `3200`, `6.50` → `6.5`. Never uses scientific notation, unlike `Double.toString()`,
 * so the result can be put back into a text field and parsed again.
 */
fun Double.toPlainString(maxDecimals: Int = 2): String {
    require(maxDecimals >= 0) { "maxDecimals must be >= 0" }
    val factor = 10.0.pow(maxDecimals).roundToLong()
    val scaled = abs(this * factor).roundToLong()
    val integer = scaled / factor
    val fraction = (scaled % factor).toString().padStart(maxDecimals, '0').trimEnd('0')
    val sign = if (this < 0 && scaled != 0L) "-" else ""
    return if (fraction.isEmpty()) "$sign$integer" else "$sign$integer.$fraction"
}

/** `85.0` → `85 %`, `97.5` → `97.5 %`. */
fun Double.percent(): String = "${toPlainString(1)} %"
