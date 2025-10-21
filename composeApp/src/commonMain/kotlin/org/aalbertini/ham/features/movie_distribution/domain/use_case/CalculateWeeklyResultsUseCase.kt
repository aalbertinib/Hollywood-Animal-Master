package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieDistributionCalculator

/**
 * Use case for calculating weekly distribution results
 */
class CalculateWeeklyResultsUseCase {
    operator fun invoke(
        commercialScore: Double,
        availableScreenings: Double,
        availableScreeningsOverrides: Map<Int, Double> = emptyMap(),
        weekMultiplierOverrides: Map<Int, Double> = emptyMap()
    ): List<Long> {
        val rawResults = if (availableScreeningsOverrides.isNotEmpty() || weekMultiplierOverrides.isNotEmpty()) {
            MovieDistributionCalculator.calculateWeeklyResultsWithOverrides(
                commercialScore = commercialScore,
                availableScreenings = availableScreenings,
                availableScreeningsOverrides = availableScreeningsOverrides,
                weekMultiplierOverrides = weekMultiplierOverrides
            )
        } else {
            MovieDistributionCalculator.calculateWeeklyResults(
                commercialScore = commercialScore,
                availableScreenings = availableScreenings
            )
        }
        return MovieDistributionCalculator.applyRoundingRules(rawResults)
    }
}
