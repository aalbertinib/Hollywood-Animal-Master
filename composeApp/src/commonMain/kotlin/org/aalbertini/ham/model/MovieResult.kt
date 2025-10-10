package org.aalbertini.ham.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Represents a saved movie result with its inputs and metadata
 */
@OptIn(ExperimentalTime::class)
@Serializable
data class MovieResult(
    val id: String,
    val title: String,
    val commercialScore: Double, // commercialScore
    val numberOfScreenings: Double, // availableScreenings
    val availableScreeningsOverrides: Map<Int, Double> = emptyMap(), // Per-week availableScreenings overrides (week index -> availableScreenings value)
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)

/**
 * Container for all saved movie results
 */
@Serializable
data class MovieResultsList(
    val movieResults: List<MovieResult> = emptyList()
)
