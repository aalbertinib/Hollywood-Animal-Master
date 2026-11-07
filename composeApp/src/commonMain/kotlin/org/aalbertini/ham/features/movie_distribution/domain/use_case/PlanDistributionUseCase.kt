package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.domain.calculator.DistributionPlanner
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionPlan
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult

/**
 * Use case for building the week-by-week screening plan of a movie
 */
class PlanDistributionUseCase {
    operator fun invoke(movie: MovieResult): DistributionPlan = DistributionPlanner.plan(movie)
}
