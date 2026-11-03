package org.aalbertini.ham.features.movie_distribution.domain.model

/** Weeks 1–4 are booked at release; later weeks are an extension you set up separately. */
enum class RunPhase { INITIAL, EXTENSION }

/** Where a week's demand figure comes from. */
enum class DemandSource {
    /** Straight from the selected method. */
    FORMULA,

    /** Method curve rescaled to the attendance measured in an earlier week. */
    CALIBRATED,

    /** Week already ran: figures are what the game reported. */
    ACTUAL,
}

/**
 * One week of the plan.
 *
 * @property index 0-based week index (week number = index + 1).
 * @property demand Total screenings the audience should fill this week.
 * @property ownScreenings Screenings your own cinemas provide this week.
 * @property toRent Screenings to rent from other cinemas: `demand − own`, never negative.
 * For an [DemandSource.ACTUAL] week, what was really rented.
 * @property idleOwnScreenings Own screenings expected to stay empty (own > demand).
 */
data class WeekPlan(
    val index: Int,
    val phase: RunPhase,
    val demand: Long,
    val ownScreenings: Long,
    val toRent: Long,
    val source: DemandSource,
    val ownOverridden: Boolean,
    val actual: WeekActual?,
) {
    val number: Int get() = index + 1
    val idleOwnScreenings: Long get() = (ownScreenings - demand).coerceAtLeast(0)
}

data class DistributionPlan(
    val method: DistributionMethod,
    val weeks: List<WeekPlan>,
) {
    val totalDemand: Long get() = weeks.sumOf { it.demand }
    val totalRent: Long get() = weeks.sumOf { it.toRent }
    val peakDemand: Long get() = weeks.maxOfOrNull { it.demand } ?: 0

    /** First week with the highest demand: usually week 1, not always once real results are in. */
    val peakWeek: WeekPlan? get() = weeks.maxByOrNull { it.demand }
    val extensionRent: Long get() = weeks.filter { it.phase == RunPhase.EXTENSION }.sumOf { it.toRent }
    val isCalibrated: Boolean get() = weeks.any { it.source != DemandSource.FORMULA }
}
