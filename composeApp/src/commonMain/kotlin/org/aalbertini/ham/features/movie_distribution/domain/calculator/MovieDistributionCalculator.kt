package org.aalbertini.ham.features.movie_distribution.domain.calculator

import kotlin.math.ceil
import kotlin.math.floor

object MovieDistributionCalculator {
    /**
     * Computes weekly results based on the provided formulas:
     * - Week 1: max(0, (commercialScore * WEEK_ONE_MULTIPLIER * BASE_MULTIPLIER) - availableScreenings)
     * - Week 2: max(0, (commercialScore * WEEK_TWO_MULTIPLIER * BASE_MULTIPLIER) - availableScreenings)
     * - Week 3..8: take Week 2 result and apply consecutive weekly reductions
     * Negative results are coerced to 0.
     */
    fun calculateWeeklyResults(commercialScore: Double, availableScreenings: Double): List<Double> {
        val week1 =
            ((commercialScore * MovieDistributionConstants.Multipliers.WEEK_ONE * MovieDistributionConstants.Multipliers.BASE) - availableScreenings).coerceAtLeast(
                0.0
            )
        val week2 =
            ((commercialScore * MovieDistributionConstants.Multipliers.WEEK_TWO * MovieDistributionConstants.Multipliers.BASE) - availableScreenings).coerceAtLeast(
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
     * Computes weekly results with per-week availableScreenings and multiplier overrides.
     * 
     * Supports two types of overrides:
     * - availableScreeningsOverrides: Override the available screenings for specific weeks
     * - weekMultiplierOverrides: Override the week multiplier for specific weeks
     * 
     * Behavior:
     * - Weeks 1-2: Apply formula (commercialScore * multiplier * BASE) - availableScreenings
     * - Weeks 3-8 without overrides: Use result reduction chain from week 2 (multiply by 0.8)
     * - Weeks 3-8 with screenings override: Apply formula with reduced revenue multiplier
     * - Weeks with multiplier override: Apply formula directly with custom multiplier
     * 
     * This maintains backward compatibility while adding week multiplier customization.
     */
    fun calculateWeeklyResultsWithOverrides(
        commercialScore: Double,
        availableScreenings: Double,
        availableScreeningsOverrides: Map<Int, Double> = emptyMap(),
        weekMultiplierOverrides: Map<Int, Double> = emptyMap()
    ): List<Double> {
        val baseRevenue = commercialScore * MovieDistributionConstants.Multipliers.BASE
        
        // Calculate week 1 with potential overrides
        val week1Multiplier = weekMultiplierOverrides[0] ?: MovieDistributionConstants.Multipliers.WEEK_ONE.toDouble()
        val availableScreeningsWeek1 = availableScreeningsOverrides[0] ?: availableScreenings
        val week1 = ((baseRevenue * week1Multiplier) - availableScreeningsWeek1).coerceAtLeast(0.0)
        
        // Calculate week 2 with potential overrides
        val week2Multiplier = weekMultiplierOverrides[1] ?: MovieDistributionConstants.Multipliers.WEEK_TWO.toDouble()
        val availableScreeningsWeek2 = availableScreeningsOverrides[1] ?: availableScreenings
        val week2 = ((baseRevenue * week2Multiplier) - availableScreeningsWeek2).coerceAtLeast(0.0)

        val results = MutableList(MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS) { 0.0 }
        results[0] = week1
        results[1] = week2

        // Calculate weeks 3-8 with override support
        // Maintains reduction chain from week 2 for backward compatibility
        var normalReduction = week2
        var revenueMultiplier = week2Multiplier
        val remainingWeeks = MovieDistributionConstants.WeeklyCalculation.NUMBER_OF_WEEKS - 
                            MovieDistributionConstants.WeeklyCalculation.REDUCTION_START_INDEX
        
        repeat(remainingWeeks) { i ->
            val weekIndex = i + MovieDistributionConstants.WeeklyCalculation.REDUCTION_START_INDEX
            
            // Apply consecutive 20% reduction to both result and revenue multiplier
            normalReduction *= MovieDistributionConstants.WeeklyCalculation.WEEKLY_REDUCTION_RATE
            revenueMultiplier *= MovieDistributionConstants.WeeklyCalculation.WEEKLY_REDUCTION_RATE
            
            // Check if this week has any overrides
            val hasMultiplierOverride = weekMultiplierOverrides.containsKey(weekIndex)
            val overrideScreenings = availableScreeningsOverrides[weekIndex]
            
            results[weekIndex] = when {
                // If week multiplier is overridden, use it directly with formula
                hasMultiplierOverride -> {
                    val customMultiplier = weekMultiplierOverrides[weekIndex] ?: revenueMultiplier
                    val weekScreenings = overrideScreenings ?: availableScreenings
                    ((baseRevenue * customMultiplier) - weekScreenings).coerceAtLeast(0.0)
                }
                // If only screenings are overridden, apply formula with reduced revenue multiplier
                overrideScreenings != null -> {
                    ((baseRevenue * revenueMultiplier) - overrideScreenings).coerceAtLeast(0.0)
                }
                // No overrides: use the normal reduction value (maintains backward compatibility)
                else -> normalReduction
            }
        }
        
        return results
    }

    /**
     * Computes weekly results ignoring availableScreenings (as requested):
     * - Week 1: max(0, commercialScore * WEEK_ONE_MULTIPLIER * BASE_MULTIPLIER)
     * - Week 2: max(0, commercialScore * WEEK_TWO_MULTIPLIER * BASE_MULTIPLIER)
     * - Week 3..8: take Week 2 result and apply consecutive weekly reductions
     * Negative results are coerced to 0.
     */
    fun calculateWeeklyResultsIgnoreAvailableScreenings(commercialScore: Double): List<Double> {
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
