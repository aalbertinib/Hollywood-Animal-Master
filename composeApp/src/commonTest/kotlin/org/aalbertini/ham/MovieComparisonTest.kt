package org.aalbertini.ham

import org.aalbertini.ham.features.movie_distribution.domain.calculator.DistributionPlanner
import org.aalbertini.ham.features.movie_distribution.domain.calculator.MovieComparison
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionMethod
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.domain.model.WeekActual
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MovieComparisonTest {

    private fun movie(id: String, method: DistributionMethod, runWeeks: Int = 8) = MovieResult(
        id = id,
        title = id,
        commercialScore = 5.0,
        numberOfScreenings = 3200.0,
        method = method,
        runWeeks = runWeeks,
    )

    @Test
    fun sameMovie_twoMethods_differenceIsAgainstTheFirst() {
        val steam = movie("steam", DistributionMethod.STEAM_GUIDE)
        val fast = movie("fast", DistributionMethod.FAST_DECAY)
        val comparison = MovieComparison(listOf(fast, steam))

        assertEquals(0, comparison.totalRentDelta(0))
        val expected = DistributionPlanner.plan(steam).totalRent - DistributionPlanner.plan(fast).totalRent
        assertEquals(expected, comparison.totalRentDelta(1))
        // Same commercial score: both methods peak at 10,000 in week 1.
        assertEquals(0, comparison.peakDelta(1))
    }

    @Test
    fun differentRunLengths_tableCoversTheLongest_andShortRunsHaveGaps() {
        val comparison = MovieComparison(
            listOf(movie("a", DistributionMethod.FAST_DECAY, runWeeks = 4), movie("b", DistributionMethod.FAST_DECAY, runWeeks = 12))
        )
        assertEquals(12, comparison.weekCount)
        assertNull(comparison.week(0, 4))
        assertEquals(5, comparison.week(1, 4)?.number)
    }

    @Test
    fun barScale_isSharedAcrossMovies() {
        val small = movie("small", DistributionMethod.FAST_DECAY)
        val big = small.copy(id = "big", commercialScore = 9.0)
        val comparison = MovieComparison(listOf(small, big))
        assertEquals(DistributionPlanner.plan(big).weeks.maxOf { it.toRent }, comparison.maxWeeklyRent)
    }

    @Test
    fun weeksRan_countsEnteredResults() {
        val ran = movie("ran", DistributionMethod.FAST_DECAY)
            .copy(actuals = mapOf(0 to WeekActual(9000, 90.0), 1 to WeekActual(5000, 70.0)))
        val comparison = MovieComparison(listOf(ran, movie("new", DistributionMethod.FAST_DECAY)))
        assertEquals(2, comparison.weeksRan(0))
        assertEquals(0, comparison.weeksRan(1))
    }
}
