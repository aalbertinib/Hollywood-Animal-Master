package org.aalbertini.ham

import kotlin.math.ceil
import kotlin.math.floor

object MovieDistributionCalculator {
    /**
     * Computes weekly results based on the provided formulas:
     * - Week 1: max(0, (p1 * WEEK_ONE_MULTIPLIER * BASE_MULTIPLIER) - p2)
     * - Week 2: max(0, (p1 * WEEK_TWO_MULTIPLIER * BASE_MULTIPLIER) - p2)
     * - Week 3..8: take Week 2 result and apply consecutive weekly reductions
     * Negative results are coerced to 0.
     */
    fun calculateWeeklyResults(p1: Double, p2: Double): List<Double> {
        val week1 =
            ((p1 * MovieDistributionConstants.Multipliers.WEEK_ONE * MovieDistributionConstants.Multipliers.BASE) - p2).coerceAtLeast(
                0.0
            )
        val week2 =
            ((p1 * MovieDistributionConstants.Multipliers.WEEK_TWO * MovieDistributionConstants.Multipliers.BASE) - p2).coerceAtLeast(
                0.0
            )

        val results =
            MutableList(MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS) { 0.0 }
        results[0] = week1
        results[1] = week2

        var current = week2
        val remainingWeeks =
            MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS - MovieDistributionConstants.WeeklyCalculation.REDUCTION_START_INDEX
        repeat(remainingWeeks) { i ->
            current *= MovieDistributionConstants.WeeklyCalculation.WEEKLY_REDUCTION_RATE
            results[i + MovieDistributionConstants.WeeklyCalculation.REDUCTION_START_INDEX] =
                current
        }
        return results
    }

    /**
     * Computes weekly results ignoring p2 (as requested):
     * - Week 1: max(0, p1 * WEEK_ONE_MULTIPLIER * BASE_MULTIPLIER)
     * - Week 2: max(0, p1 * WEEK_TWO_MULTIPLIER * BASE_MULTIPLIER)
     * - Week 3..8: take Week 2 result and apply consecutive weekly reductions
     * Negative results are coerced to 0.
     */
    fun calculateWeeklyResultsIgnoreP2(p1: Double): List<Double> {
        val week1 =
            (p1 * MovieDistributionConstants.Multipliers.WEEK_ONE * MovieDistributionConstants.Multipliers.BASE).coerceAtLeast(
                0.0
            )
        val week2 =
            (p1 * MovieDistributionConstants.Multipliers.WEEK_TWO * MovieDistributionConstants.Multipliers.BASE).coerceAtLeast(
                0.0
            )

        val results =
            MutableList(MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS) { 0.0 }
        results[0] = week1
        results[1] = week2

        var current = week2
        val remainingWeeks =
            MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS - MovieDistributionConstants.WeeklyCalculation.REDUCTION_START_INDEX
        repeat(remainingWeeks) { i ->
            current *= MovieDistributionConstants.WeeklyCalculation.WEEKLY_REDUCTION_RATE
            results[i + MovieDistributionConstants.WeeklyCalculation.REDUCTION_START_INDEX] =
                current
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
            if (index < MovieDistributionConstants.Rounding.ROUND_UP_UNTIL_INDEX) {
                ceil(v).toLong()
            } else {
                floor(v).toLong()
            }
        }
    }
}