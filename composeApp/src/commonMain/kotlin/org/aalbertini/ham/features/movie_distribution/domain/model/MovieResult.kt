package org.aalbertini.ham.features.movie_distribution.domain.model

import kotlinx.serialization.Serializable

/**
 * A saved movie and everything needed to rebuild its distribution plan.
 *
 * Storage compatibility: V1 saves only contain `id`, `title`, `commercialScore`,
 * `numberOfScreenings`, `availableScreeningsOverrides` (and `weekMultiplierOverrides` on the
 * tech/new-ui branch, now ignored). Every field added in V2 has a default so those files still
 * decode, and V1 movies keep the method they were computed with ([DistributionMethod.STEAM_GUIDE]).
 *
 * @property commercialScore In-game commercial ("popcorn") score, 0.1–10.
 * @property artisticScore In-game artistic score. Only used by methods that average both scores.
 * @property numberOfScreenings Screenings your own cinemas provide each week (kept under its V1 name).
 * @property availableScreeningsOverrides Week index (0-based) → own screenings for that week only,
 * e.g. when another of your films occupies part of your cinemas.
 * @property actuals Week index (0-based) → what the game reported after that week ran.
 * @property weekOneBonus Extra screenings added to week 1 (holiday release, strong ad campaign).
 * @property runWeeks How many weeks to plan: 4 (initial run), 8 or 12 (with extensions).
 */
@Serializable
data class MovieResult(
    val id: String,
    val title: String,
    val commercialScore: Double,
    val numberOfScreenings: Double,
    val availableScreeningsOverrides: Map<Int, Double> = emptyMap(),
    val artisticScore: Double? = null,
    val method: DistributionMethod = DistributionMethod.STEAM_GUIDE,
    val customCurve: CustomCurve = CustomCurve(),
    val weekOneBonus: Long = 0,
    val runWeeks: Int = DistributionDefaults.RUN_WEEKS,
    val actuals: Map<Int, WeekActual> = emptyMap(),
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
)

/**
 * Container for all saved movie results
 */
@Serializable
data class MovieResultsList(
    val movieResults: List<MovieResult> = emptyList()
)

/**
 * What the in-game "Distribution results" table showed for one week.
 *
 * @property screenings Total screenings that ran that week (own + rented).
 * @property attendancePercent Attendance column, 0–100.
 */
@Serializable
data class WeekActual(
    val screenings: Long,
    val attendancePercent: Double,
) {
    /** Screenings that were actually filled: the audience demand the game measured. */
    val filledScreenings: Double get() = screenings * attendancePercent / 100.0

    /** At (near) full attendance demand was probably higher than what was booked. */
    val isSaturated: Boolean get() = attendancePercent >= DistributionDefaults.SATURATED_ATTENDANCE
}
