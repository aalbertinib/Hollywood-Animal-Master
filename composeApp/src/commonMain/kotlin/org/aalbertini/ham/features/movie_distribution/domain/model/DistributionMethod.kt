package org.aalbertini.ham.features.movie_distribution.domain.model

import kotlinx.serialization.Serializable

/**
 * Community methods for sizing a release, all expressed in screenings per week.
 * None of them is an official formula: Weappy has never published one. Sources are listed in
 * the in-app guide and in docs/V2_AUDIT.md.
 */
@Serializable
enum class DistributionMethod {
    /**
     * SomeGuyOnPhone's Steam guide (Oct 2025), the formula V1 used:
     * week 1 = commercial × 2000, week 2 = commercial × 1000, then −20 % per week.
     */
    STEAM_GUIDE,

    /**
     * Judge Mental's guidelines (Steam discussions, Apr–Jun 2026):
     * base = average of commercial and artistic, week 1 = base × 2000,
     * week 2 = week 1 / 2 + 1000, weeks 3–4 halve, week 5 = week 1 / 8, then halve.
     * Matches the attendance drop other players report (≈90 / 45 / 20 / 15 %).
     */
    FAST_DECAY,

    /** nosavynada (Dec 2025): week 1 = commercial × 2000, then −⅓ every week. */
    TWO_THIRDS,

    /** User-defined curve, see [CustomCurve]. */
    CUSTOM,
}

/**
 * Parameters of a [DistributionMethod.CUSTOM] curve.
 *
 * @property weekOneMultiplier Week 1 = score × 1000 × this.
 * @property weekTwoRatio Week 2 = week 1 × this.
 * @property weeklyDecay Every following week = previous week × this.
 * @property averageScores Use the average of commercial and artistic scores instead of commercial only.
 */
@Serializable
data class CustomCurve(
    val weekOneMultiplier: Double = 2.0,
    val weekTwoRatio: Double = 0.5,
    val weeklyDecay: Double = 0.8,
    val averageScores: Boolean = false,
)

object DistributionDefaults {
    /** One score point ≈ 1000 screenings of demand (the "popcorn × 1000" rule). */
    const val SCREENINGS_PER_SCORE_POINT = 1000.0

    /** Weeks 1–4 are booked together at release; later weeks come from "Setup distribution". */
    const val INITIAL_RUN_WEEKS = 4
    const val RUN_WEEKS = 8
    val RUN_WEEK_OPTIONS = listOf(4, 8, 12)

    /** Attendance at or above this is treated as "sold out": real demand may be higher. */
    const val SATURATED_ATTENDANCE = 97.0

    const val SCORE_MIN = 0.1
    const val SCORE_MAX = 10.0
    const val SCREENINGS_MAX = 1_000_000L

    const val DEFAULT_COMMERCIAL_SCORE = 5.0
    const val DEFAULT_OWN_SCREENINGS = 3_200L

    /** Method suggested for new movies (see the guide for why). */
    val DEFAULT_METHOD = DistributionMethod.FAST_DECAY
}
