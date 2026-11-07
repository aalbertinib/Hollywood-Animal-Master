package org.aalbertini.ham

import kotlinx.serialization.json.Json
import org.aalbertini.ham.features.movie_distribution.domain.calculator.DistributionPlanner
import org.aalbertini.ham.features.movie_distribution.domain.model.CustomCurve
import org.aalbertini.ham.features.movie_distribution.domain.model.DemandSource
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionMethod
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResultsList
import org.aalbertini.ham.features.movie_distribution.domain.model.RunPhase
import org.aalbertini.ham.features.movie_distribution.domain.model.WeekActual
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DistributionPlannerTest {

    private val lenientJson = Json { ignoreUnknownKeys = true }

    private fun movie(
        commercial: Double,
        own: Double = 0.0,
        method: DistributionMethod = DistributionMethod.STEAM_GUIDE,
        artistic: Double? = null,
    ) = MovieResult(
        id = "test",
        title = "Test",
        commercialScore = commercial,
        numberOfScreenings = own,
        artisticScore = artistic,
        method = method,
    )

    private fun MovieResult.rent() = DistributionPlanner.plan(this).weeks.map { it.toRent }
    private fun MovieResult.demand() = DistributionPlanner.plan(this).weeks.map { it.demand }

    @Test
    fun steamGuide_withoutOwnCinemas_matchesV1() {
        // V1 gave 3000, 1500, 1200, 960, 768, 614, 491, 393 for these inputs.
        assertEquals(listOf(3000L, 1500, 1200, 960, 768, 614, 491, 393), movie(1.5).rent())
    }

    @Test
    fun steamGuide_subtractsOwnScreeningsEveryWeek() {
        // Demand 3000, 1500, 1200, 960, 768, 614.4, 491.52, 393.216 minus 200 own each week.
        // V1 decayed (1500 − 200) instead and returned 1040, 832, 665, 532, 425, 340 for weeks 3–8.
        assertEquals(listOf(2800L, 1300, 1000, 760, 568, 414, 291, 193), movie(1.5, own = 200.0).rent())
    }

    @Test
    fun ownScreeningsAboveDemand_rentNothing() {
        val plan = DistributionPlanner.plan(movie(5.0, own = 3200.0))
        // Demand 10000, 5000, 4000, 3200, 2560 ...
        assertEquals(listOf(6800L, 1800, 800, 0, 0, 0, 0, 0), plan.weeks.map { it.toRent })
        assertEquals(640, plan.weeks[4].idleOwnScreenings)
    }

    @Test
    fun fastDecay_matchesJudgeMentalExample() {
        // "6.0/4.0 movie is a 5.0": 10,000 / 6,000 / 3,000 / 1,500 / 1,250 / 625 / ~300 / ~150
        val demand = movie(6.0, method = DistributionMethod.FAST_DECAY, artistic = 4.0).demand()
        assertEquals(listOf(10000L, 6000, 3000, 1500, 1250, 625, 312, 156), demand)
    }

    @Test
    fun fastDecay_withoutArtisticScore_usesCommercialOnly() {
        assertEquals(12000L, movie(6.0, method = DistributionMethod.FAST_DECAY).demand().first())
    }

    @Test
    fun twoThirds_decaysByAThirdEachWeek() {
        val demand = movie(3.0, method = DistributionMethod.TWO_THIRDS).demand()
        // 6000, 4000, 2666.7 (ceil), 1777.8 (ceil), 1185.2 (floor) ...
        assertEquals(listOf(6000L, 4000, 2667, 1778, 1185, 790, 526, 351), demand)
    }

    @Test
    fun custom_curveIsApplied() {
        val custom = movie(4.0, method = DistributionMethod.CUSTOM).copy(
            customCurve = CustomCurve(weekOneMultiplier = 3.0, weekTwoRatio = 0.6, weeklyDecay = 0.5),
            runWeeks = 4,
        )
        assertEquals(listOf(12000L, 7200, 3600, 1800), custom.demand())
    }

    @Test
    fun weekOneBonus_onlyAffectsWeekOne() {
        val demand = movie(5.0).copy(weekOneBonus = 3000).demand()
        assertEquals(13000L, demand[0])
        assertEquals(5000L, demand[1])
    }

    @Test
    fun ownOverride_appliesToThatWeekOnly() {
        val plan = DistributionPlanner.plan(
            movie(5.0, own = 3200.0).copy(availableScreeningsOverrides = mapOf(2 to 0.0))
        )
        assertEquals(4000, plan.weeks[2].toRent)
        assertTrue(plan.weeks[2].ownOverridden)
        assertEquals(0, plan.weeks[3].toRent)
    }

    @Test
    fun actualResults_rescaleFollowingWeeks() {
        // Week 1 forecast 10,000; the game filled 60 % of 10,000 → demand is 60 % of the curve.
        val plan = DistributionPlanner.plan(
            movie(5.0).copy(actuals = mapOf(0 to WeekActual(screenings = 10_000, attendancePercent = 60.0)))
        )
        assertEquals(DemandSource.ACTUAL, plan.weeks[0].source)
        assertEquals(6000, plan.weeks[0].demand)
        assertEquals(DemandSource.CALIBRATED, plan.weeks[1].source)
        assertEquals(3000, plan.weeks[1].demand)
        assertEquals(2400, plan.weeks[2].demand)
    }

    @Test
    fun soldOutWeek_neverScalesForecastDown() {
        val plan = DistributionPlanner.plan(
            movie(5.0).copy(actuals = mapOf(0 to WeekActual(screenings = 8_000, attendancePercent = 100.0)))
        )
        assertEquals(5000, plan.weeks[1].demand)
    }

    @Test
    fun soldOutWeek_keepsEarlierLowerEstimate() {
        // Forecast 10,000 / 5,000 / 4,000. Week 1 half full → ×0.5.
        // Week 2 sold out at 3,500 → ×0.7: only a lower bound, but higher than ×0.5, so it wins.
        val plan = DistributionPlanner.plan(
            movie(5.0).copy(
                actuals = mapOf(
                    0 to WeekActual(screenings = 10_000, attendancePercent = 50.0),
                    1 to WeekActual(screenings = 3_500, attendancePercent = 100.0),
                )
            )
        )
        assertEquals(2800, plan.weeks[2].demand)
    }

    @Test
    fun soldOutWeek_belowEarlierEstimate_keepsEarlierEstimate() {
        // Week 1 at ×1.2, week 2 sold out with too few bookings (×0.4): stay at ×1.2.
        val plan = DistributionPlanner.plan(
            movie(5.0).copy(
                actuals = mapOf(
                    0 to WeekActual(screenings = 12_000, attendancePercent = 100.0),
                    1 to WeekActual(screenings = 2_000, attendancePercent = 100.0),
                )
            )
        )
        assertEquals(4800, plan.weeks[2].demand)
        assertEquals(DemandSource.CALIBRATED, plan.weeks[2].source)
    }

    @Test
    fun latestActualWins() {
        val plan = DistributionPlanner.plan(
            movie(5.0).copy(
                actuals = mapOf(
                    0 to WeekActual(10_000, 50.0), // ratio 0.5
                    1 to WeekActual(5_000, 80.0),  // 4000 / 5000 → ratio 0.8
                )
            )
        )
        assertEquals(3200, plan.weeks[2].demand) // 4000 × 0.8
    }

    @Test
    fun phasesAndRunLength() {
        val plan = DistributionPlanner.plan(movie(5.0).copy(runWeeks = 12))
        assertEquals(12, plan.weeks.size)
        assertEquals(RunPhase.INITIAL, plan.weeks[3].phase)
        assertEquals(RunPhase.EXTENSION, plan.weeks[4].phase)
    }

    @Test
    fun v1SaveFile_stillDecodes() {
        val v1 = """
            {"movieResults":[{"id":"1-12345","title":"Old","commercialScore":7.0,"numberOfScreenings":3200.0,
            "availableScreeningsOverrides":{"2":1000.0},"weekMultiplierOverrides":{},
            "createdAt":1,"updatedAt":2}]}
        """.trimIndent()
        val movie = lenientJson.decodeFromString<MovieResultsList>(v1).movieResults.single()
        assertEquals(DistributionMethod.STEAM_GUIDE, movie.method)
        assertEquals(8, movie.runWeeks)
        assertEquals(mapOf(2 to 1000.0), movie.availableScreeningsOverrides)
    }
}
