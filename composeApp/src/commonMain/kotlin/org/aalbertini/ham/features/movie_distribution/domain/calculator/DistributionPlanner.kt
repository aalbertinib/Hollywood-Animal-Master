package org.aalbertini.ham.features.movie_distribution.domain.calculator

import org.aalbertini.ham.features.movie_distribution.domain.model.CustomCurve
import org.aalbertini.ham.features.movie_distribution.domain.model.DemandSource
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionDefaults
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionMethod
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionPlan
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.domain.model.RunPhase
import org.aalbertini.ham.features.movie_distribution.domain.model.WeekPlan
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToLong

/**
 * Builds a week-by-week screening plan for a movie.
 *
 * 1. The selected [DistributionMethod] gives the audience demand curve (total screenings per week).
 * 2. Weeks that already ran ([MovieResult.actuals]) replace the curve with what the game measured
 *    (screenings × attendance), and every later week is rescaled by the same measured/forecast ratio.
 * 3. Own screenings are subtracted from demand **every week**; the rest is what to rent.
 *
 * V1 subtracted own screenings once (in week 2) and then decayed that difference, so weeks 3–8
 * over-rented by `own × (1 − 0.8ⁿ)`. Subtracting per week fixes it.
 */
object DistributionPlanner {

    fun plan(movie: MovieResult): DistributionPlan {
        val runWeeks = movie.runWeeks.coerceIn(1, DistributionDefaults.RUN_WEEK_OPTIONS.max())
        val curve = demandCurve(movie, runWeeks)
        var calibration = 1.0
        val weeks = curve.mapIndexed { index, forecast ->
            val ownOverride = movie.availableScreeningsOverrides[index]
            val own = (ownOverride ?: movie.numberOfScreenings).coerceAtLeast(0.0).roundToLong()
            val phase = if (index < DistributionDefaults.INITIAL_RUN_WEEKS) RunPhase.INITIAL else RunPhase.EXTENSION
            val actual = movie.actuals[index]
            if (actual != null) {
                if (forecast > 0.0) {
                    val measured = actual.filledScreenings / forecast
                    // Sold out only proves the audience was at least this big: keep the earlier
                    // estimate when it is higher, rather than falling back to the raw formula.
                    calibration = if (actual.isSaturated) maxOf(measured, calibration) else measured
                }
                WeekPlan(
                    index = index,
                    phase = phase,
                    demand = actual.filledScreenings.roundToLong(),
                    ownScreenings = own,
                    toRent = (actual.screenings - own).coerceAtLeast(0),
                    source = DemandSource.ACTUAL,
                    ownOverridden = ownOverride != null,
                    actual = actual,
                )
            } else {
                val demand = forecast * calibration
                WeekPlan(
                    index = index,
                    phase = phase,
                    demand = round(demand, phase),
                    ownScreenings = own,
                    toRent = round((demand - own).coerceAtLeast(0.0), phase),
                    source = if (calibration == 1.0) DemandSource.FORMULA else DemandSource.CALIBRATED,
                    ownOverridden = ownOverride != null,
                    actual = null,
                )
            }
        }
        return DistributionPlan(movie.method, weeks)
    }

    /**
     * Audience demand per week, in screenings, before calibration and before own cinemas.
     * [MovieResult.weekOneBonus] is added to week 1 only (holiday / ad push).
     */
    fun demandCurve(movie: MovieResult, runWeeks: Int = movie.runWeeks): List<Double> {
        val perPoint = DistributionDefaults.SCREENINGS_PER_SCORE_POINT
        val commercial = movie.commercialScore
        val averaged = movie.artisticScore?.let { (commercial + it) / 2.0 } ?: commercial
        val curve = when (movie.method) {
            DistributionMethod.STEAM_GUIDE -> {
                val weekOne = commercial * 2 * perPoint
                val weekTwo = commercial * perPoint
                generate(runWeeks, weekOne) { week, previous -> if (week == 1) weekTwo else previous * 0.8 }
            }

            DistributionMethod.FAST_DECAY -> {
                val weekOne = averaged * 2 * perPoint
                generate(runWeeks, weekOne) { week, previous ->
                    when (week) {
                        1 -> previous / 2 + perPoint
                        DistributionDefaults.INITIAL_RUN_WEEKS -> weekOne / 8
                        else -> previous / 2
                    }
                }
            }

            DistributionMethod.TWO_THIRDS -> {
                generate(runWeeks, commercial * 2 * perPoint) { _, previous -> previous * 2 / 3 }
            }

            DistributionMethod.CUSTOM -> custom(movie.customCurve, commercial, averaged, runWeeks)
        }
        return curve.mapIndexed { index, demand ->
            if (index == 0) demand + movie.weekOneBonus.coerceAtLeast(0) else demand
        }
    }

    private fun custom(curve: CustomCurve, commercial: Double, averaged: Double, runWeeks: Int): List<Double> {
        val score = if (curve.averageScores) averaged else commercial
        val weekOne = score * curve.weekOneMultiplier * DistributionDefaults.SCREENINGS_PER_SCORE_POINT
        return generate(runWeeks, weekOne) { week, previous ->
            if (week == 1) previous * curve.weekTwoRatio else previous * curve.weeklyDecay
        }
    }

    /** `next(weekIndex, previousValue)` produces week `weekIndex` (0-based) from the one before. */
    private inline fun generate(runWeeks: Int, weekOne: Double, next: (Int, Double) -> Double): List<Double> {
        val values = ArrayList<Double>(runWeeks)
        var current = weekOne
        repeat(runWeeks) { week ->
            if (week > 0) current = next(week, current)
            values += current.coerceAtLeast(0.0)
        }
        return values
    }

    /**
     * Initial-run weeks round up (missing seats at release costs the most);
     * extension weeks round down (renting late is rarely worth it). Same rule as V1.
     */
    private fun round(value: Double, phase: RunPhase): Long =
        if (phase == RunPhase.INITIAL) ceil(value - EPSILON).toLong() else floor(value + EPSILON).toLong()

    /** Absorbs floating-point noise such as 800.0000000001 so it doesn't round up to 801. */
    private const val EPSILON = 1e-6
}
