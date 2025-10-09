package org.aalbertini.ham.model

import kotlinx.serialization.Serializable

/**
 * Represents a saved movie result with its inputs and metadata
 */
@Serializable
data class MovieResult(
    val id: String,
    val title: String,
    val commercialScore: Double, // p1
    val numberOfSeats: Double, // p2
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Container for all saved movie results
 */
@Serializable
data class MovieResultsList(
    val movieResults: List<MovieResult> = emptyList()
)
