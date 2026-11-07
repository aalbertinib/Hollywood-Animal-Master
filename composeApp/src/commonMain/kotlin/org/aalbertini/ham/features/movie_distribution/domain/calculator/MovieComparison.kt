package org.aalbertini.ham.features.movie_distribution.domain.calculator

import org.aalbertini.ham.features.movie_distribution.domain.model.DemandSource
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionPlan
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.domain.model.WeekPlan

/**
 * Side-by-side plans for a few saved movies. The first movie is the reference the others
 * are measured against, so "duplicate, change the method, compare" reads as a difference.
 */
class MovieComparison(val movies: List<MovieResult>) {

    val plans: List<DistributionPlan> = movies.map(DistributionPlanner::plan)

    /** Rows of the weekly table: the longest run among the movies. */
    val weekCount: Int = plans.maxOfOrNull { it.weeks.size } ?: 0

    /** Largest weekly rent across every movie, so all cells share one bar scale. */
    val maxWeeklyRent: Long = plans.maxOfOrNull { plan -> plan.weeks.maxOfOrNull { it.toRent } ?: 0 } ?: 0

    /** `null` when that movie's run is shorter than [weekIndex]. */
    fun week(movie: Int, weekIndex: Int): WeekPlan? = plans[movie].weeks.getOrNull(weekIndex)

    /** Total to rent minus the first movie's; 0 for the first movie itself. */
    fun totalRentDelta(movie: Int): Long = plans[movie].totalRent - plans.first().totalRent

    fun peakDelta(movie: Int): Long = plans[movie].peakDemand - plans.first().peakDemand

    /** Weeks that already ran in the game (entered results). */
    fun weeksRan(movie: Int): Int = plans[movie].weeks.count { it.source == DemandSource.ACTUAL }

    companion object {
        const val MIN_MOVIES = 2
        const val MAX_MOVIES = 4
    }
}
