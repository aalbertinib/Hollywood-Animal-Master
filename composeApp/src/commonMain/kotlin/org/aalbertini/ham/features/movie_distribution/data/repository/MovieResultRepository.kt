package org.aalbertini.ham.features.movie_distribution.data.repository

import org.aalbertini.ham.features.movie_distribution.data.data_source.MovieResultDataSource
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Repository for managing movie results
 * Coordinates data operations through the data source
 * Uses kotlin.time.Clock from standard library (Kotlin 2.1.20+)
 */
@OptIn(ExperimentalTime::class)
class MovieResultRepository(
    private val dataSource: MovieResultDataSource = MovieResultDataSource()
) {
    
    /**
     * Loads all saved movie results
     */
    fun loadMovieResults(): List<MovieResult> {
        return dataSource.loadAll()
    }
    
    /**
     * Saves a new movie result
     */
    fun saveMovieResult(
        title: String, 
        commercialScore: Double, 
        numberOfScreenings: Double,
        availableScreeningsOverrides: Map<Int, Double> = emptyMap(),
        weekMultiplierOverrides: Map<Int, Double> = emptyMap()
    ): MovieResult {
        val movieResults = dataSource.loadAll().toMutableList()
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val newMovieResult = MovieResult(
            id = generateId(),
            title = title,
            commercialScore = commercialScore,
            numberOfScreenings = numberOfScreenings,
            availableScreeningsOverrides = availableScreeningsOverrides,
            weekMultiplierOverrides = weekMultiplierOverrides,
            createdAt = currentTime,
            updatedAt = currentTime
        )
        movieResults.add(newMovieResult)
        dataSource.saveAll(movieResults)
        return newMovieResult
    }
    
    /**
     * Generates a unique ID for movie results
     * Uses kotlin.time.Clock from standard library for cross-platform timestamp
     */
    private fun generateId(): String {
        return "${Clock.System.now().toEpochMilliseconds()}-${Random.nextInt(10000, 99999)}"
    }
    
    /**
     * Updates an existing movie result
     */
    fun updateMovieResult(
        id: String,
        title: String,
        commercialScore: Double,
        numberOfScreenings: Double,
        availableScreeningsOverrides: Map<Int, Double> = emptyMap(),
        weekMultiplierOverrides: Map<Int, Double> = emptyMap()
    ): Boolean {
        val movieResults = dataSource.loadAll().toMutableList()
        val index = movieResults.indexOfFirst { it.id == id }
        if (index != -1) {
            val updated = movieResults[index].copy(
                title = title,
                commercialScore = commercialScore,
                numberOfScreenings = numberOfScreenings,
                availableScreeningsOverrides = availableScreeningsOverrides,
                weekMultiplierOverrides = weekMultiplierOverrides,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
            movieResults[index] = updated
            dataSource.saveAll(movieResults)
            return true
        }
        return false
    }
    
    /**
     * Deletes a movie result
     */
    fun deleteMovieResult(id: String): Boolean {
        val movieResults = dataSource.loadAll().toMutableList()
        val initialSize = movieResults.size
        movieResults.removeAll { it.id == id }
        val removed = movieResults.size < initialSize
        if (removed) {
            dataSource.saveAll(movieResults)
        }
        return removed
    }
    
    /**
     * Gets a movie result by ID
     */
    fun getMovieResultById(id: String): MovieResult? {
        return dataSource.loadAll().firstOrNull { it.id == id }
    }
    
    /**
     * Gets a movie result by title (case-insensitive)
     */
    fun getMovieResultByTitle(title: String): MovieResult? {
        return dataSource.loadAll().firstOrNull { it.title.equals(title, ignoreCase = true) }
    }
    
    /**
     * Clears all saved movie results
     */
    fun clearAllMovieResults() {
        dataSource.clearAll()
    }
}
