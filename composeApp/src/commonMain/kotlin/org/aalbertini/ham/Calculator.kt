package org.aalbertini.ham

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Computes weekly results based on the provided formulas:
 * - Week 1: abs((p1 * WEEK_ONE_MULTIPLIER * BASE_MULTIPLIER) - p2)
 * - Week 2: abs((p1 * WEEK_TWO_MULTIPLIER * BASE_MULTIPLIER) - p2)
 * - Week 3..8: take Week 2 result and apply consecutive weekly reductions
 */
fun calculateWeeklyResults(p1: Double, p2: Double): List<Double> {
    val week1 = abs((p1 * CalculationConstants.Multipliers.WEEK_ONE * CalculationConstants.Multipliers.BASE) - p2)
    val week2 = abs((p1 * CalculationConstants.Multipliers.WEEK_TWO * CalculationConstants.Multipliers.BASE) - p2)

    val results = MutableList(CalculationConstants.WeeklyCalculation.NUMBER_OF_WEEKS) { 0.0 }
    results[0] = week1
    results[1] = week2

    var current = week2
    val remainingWeeks = CalculationConstants.WeeklyCalculation.NUMBER_OF_WEEKS - CalculationConstants.WeeklyCalculation.REDUCTION_START_INDEX
    repeat(remainingWeeks) { i ->
        current *= CalculationConstants.WeeklyCalculation.WEEKLY_REDUCTION_RATE
        results[i + CalculationConstants.WeeklyCalculation.REDUCTION_START_INDEX] = current
    }
    return results
}

/**
 * Computes weekly results ignoring p2 (as requested):
 * - Week 1: abs(p1 * WEEK_ONE_MULTIPLIER * BASE_MULTIPLIER)
 * - Week 2: abs(p1 * WEEK_TWO_MULTIPLIER * BASE_MULTIPLIER)
 * - Week 3..8: take Week 2 result and apply consecutive weekly reductions
 */
fun calculateWeeklyResultsIgnoreP2(p1: Double): List<Double> {
    val week1 = abs(p1 * CalculationConstants.Multipliers.WEEK_ONE * CalculationConstants.Multipliers.BASE)
    val week2 = abs(p1 * CalculationConstants.Multipliers.WEEK_TWO * CalculationConstants.Multipliers.BASE)

    val results = MutableList(CalculationConstants.WeeklyCalculation.NUMBER_OF_WEEKS) { 0.0 }
    results[0] = week1
    results[1] = week2

    var current = week2
    val remainingWeeks = CalculationConstants.WeeklyCalculation.NUMBER_OF_WEEKS - CalculationConstants.WeeklyCalculation.REDUCTION_START_INDEX
    repeat(remainingWeeks) { i ->
        current *= CalculationConstants.WeeklyCalculation.WEEKLY_REDUCTION_RATE
        results[i + CalculationConstants.WeeklyCalculation.REDUCTION_START_INDEX] = current
    }
    return results
}

/**
 * Applies rounding rules to weekly values:
 * - Weeks 1..4 (indices 0..3): round up (ceil)
 * - Weeks 5..8 (indices 4..7): round down (floor)
 * Returns integer results as Longs
 */
fun applyRoundingRules(values: List<Double>): List<Long> {
    return values.mapIndexed { index, v ->
        if (index < CalculationConstants.Rounding.ROUND_UP_UNTIL_INDEX) {
            ceil(v).toLong()
        } else {
            floor(v).toLong()
        }
    }
}

/**
 * Filters user text input to a permissive positive decimal number format:
 * - digits only and a single decimal separator '.' or ',' (normalized to '.')
 * - minus sign is not allowed (values must be > 0)
 * - if the first kept character is '.', prefix a leading zero so parsing works (e.g. ".5" -> "0.5")
 */
fun filterNumericInput(raw: String): String {
    val normalized = raw.replace(',', '.')
    val sb = StringBuilder()
    var dotSeen = false

    normalized.forEach { c ->
        when {
            c in '0'..'9' -> sb.append(c)
            c == '.' && !dotSeen -> { sb.append('.'); dotSeen = true }
            else -> Unit // skip anything else
        }
    }
    var out = sb.toString()
    if (out.startsWith(".")) out = "0$out"
    return out
}
