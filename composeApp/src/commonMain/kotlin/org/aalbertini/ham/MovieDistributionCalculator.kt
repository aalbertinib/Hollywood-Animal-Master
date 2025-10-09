package org.aalbertini.ham

import kotlin.math.ceil
import kotlin.math.floor

object MovieDistributionCalculator {
    /**
     * Computes weekly results based on the provided formulas:
     * - Week 1: max(0, (commercialScore * WEEK_ONE_MULTIPLIER * BASE_MULTIPLIER) - availableSeats)
     * - Week 2: max(0, (commercialScore * WEEK_TWO_MULTIPLIER * BASE_MULTIPLIER) - availableSeats)
     * - Week 3..8: take Week 2 result and apply consecutive weekly reductions
     * Negative results are coerced to 0.
     */
    fun calculateWeeklyResults(commercialScore: Double, availableSeats: Double): List<Double> {
        val week1 =
            ((commercialScore * MovieDistributionConstants.Multipliers.WEEK_ONE * MovieDistributionConstants.Multipliers.BASE) - availableSeats).coerceAtLeast(
                0.0
            )
        val week2 =
            ((commercialScore * MovieDistributionConstants.Multipliers.WEEK_TWO * MovieDistributionConstants.Multipliers.BASE) - availableSeats).coerceAtLeast(
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
     * Computes weekly results with per-week availableSeats overrides
     * - availableSeatsOverrides: Map of week index (0-based) to availableSeats override value
     * - If no override is set for a week, uses the default availableSeats
     */
    fun calculateWeeklyResultsWithOverrides(commercialScore: Double, availableSeats: Double, availableSeatsOverrides: Map<Int, Double>): List<Double> {
        val baseRevenue = commercialScore * MovieDistributionConstants.Multipliers.BASE
        
        // Calculate week 1 with potential override
        val availableSeatsWeek1 = availableSeatsOverrides[0] ?: availableSeats
        val week1 = ((baseRevenue * MovieDistributionConstants.Multipliers.WEEK_ONE) - availableSeatsWeek1).coerceAtLeast(0.0)
        
        // Calculate week 2 with potential override
        val availableSeatsWeek2 = availableSeatsOverrides[1] ?: availableSeats
        val week2 = ((baseRevenue * MovieDistributionConstants.Multipliers.WEEK_TWO) - availableSeatsWeek2).coerceAtLeast(0.0)

        val results = MutableList(MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS) { 0.0 }
        results[0] = week1
        results[1] = week2

        // Calculate weeks 3-8 with independent override support
        // Reduction chain continues from week 2, independent of any overrides
        var normalReduction = week2
        val remainingWeeks = MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS - 
                            MovieDistributionConstants.WeeklyCalculation.REDUCTION_START_INDEX
        
        repeat(remainingWeeks) { i ->
            val weekIndex = i + MovieDistributionConstants.WeeklyCalculation.REDUCTION_START_INDEX
            
            // Continue normal reduction chain (always continues, regardless of overrides)
            normalReduction *= MovieDistributionConstants.WeeklyCalculation.WEEKLY_REDUCTION_RATE
            
            // Check if this specific week has a seats override
            val overrideSeats = availableSeatsOverrides[weekIndex]
            
            if (overrideSeats != null) {
                // Independent calculation for this week only (doesn't affect other weeks)
                results[weekIndex] = ((baseRevenue * MovieDistributionConstants.Multipliers.WEEK_TWO) - overrideSeats).coerceAtLeast(0.0)
            } else {
                // Use the normal reduction value (unaffected by any overrides)
                results[weekIndex] = normalReduction
            }
        }
        
        return results
    }

    /**
     * Computes weekly results ignoring availableSeats (as requested):
     * - Week 1: max(0, commercialScore * WEEK_ONE_MULTIPLIER * BASE_MULTIPLIER)
     * - Week 2: max(0, commercialScore * WEEK_TWO_MULTIPLIER * BASE_MULTIPLIER)
     * - Week 3..8: take Week 2 result and apply consecutive weekly reductions
     * Negative results are coerced to 0.
     */
    fun calculateWeeklyResultsIgnoreAvailableSeats(commercialScore: Double): List<Double> {
        val week1 =
            (commercialScore * MovieDistributionConstants.Multipliers.WEEK_ONE * MovieDistributionConstants.Multipliers.BASE).coerceAtLeast(
                0.0
            )
        val week2 =
            (commercialScore * MovieDistributionConstants.Multipliers.WEEK_TWO * MovieDistributionConstants.Multipliers.BASE).coerceAtLeast(
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