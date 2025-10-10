package org.aalbertini.ham.repository

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.aalbertini.ham.model.MovieResult
import org.aalbertini.ham.model.MovieResultsList
import org.aalbertini.ham.storage.StorageProvider
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Repository for managing movie results with persistent storage
 * Uses kotlin.time.Clock from standard library (Kotlin 2.1.20+)
 */
@OptIn(ExperimentalTime::class)
class MovieResultRepository(
    private val storageProvider: StorageProvider = StorageProvider()
) {
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    private val storageKey = "movies"
    
    /**
     * Loads all saved movie results
     */
    fun loadMovieResults(): List<MovieResult> {
        return try {
            val data = storageProvider.loadData(storageKey) ?: return emptyList()
            val movieResultsList = json.decodeFromString<MovieResultsList>(data)
            movieResultsList.movieResults
        } catch (e: Exception) {
            println("Error loading movies: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Saves a new movie result
     */
    fun saveMovieResult(
        title: String, 
        commercialScore: Double, 
        numberOfScreenings: Double,
        availableScreeningsOverrides: Map<Int, Double> = emptyMap()
    ): MovieResult {
        val movieResults = loadMovieResults().toMutableList()
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val newMovieResult = MovieResult(
            id = generateId(),
            title = title,
            commercialScore = commercialScore,
            numberOfScreenings = numberOfScreenings,
            availableScreeningsOverrides = availableScreeningsOverrides,
            createdAt = currentTime,
            updatedAt = currentTime
        )
        movieResults.add(newMovieResult)
        saveAllMovieResults(movieResults)
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
        availableScreeningsOverrides: Map<Int, Double> = emptyMap()
    ): Boolean {
        val movieResults = loadMovieResults().toMutableList()
        val index = movieResults.indexOfFirst { it.id == id }
        if (index != -1) {
            val updated = movieResults[index].copy(
                title = title,
                commercialScore = commercialScore,
                numberOfScreenings = numberOfScreenings,
                availableScreeningsOverrides = availableScreeningsOverrides,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
            movieResults[index] = updated
            saveAllMovieResults(movieResults)
            return true
        }
        return false
    }
    
    /**
     * Deletes a movie result
     */
    fun deleteMovieResult(id: String): Boolean {
        val movieResults = loadMovieResults().toMutableList()
        val initialSize = movieResults.size
        movieResults.removeAll { it.id == id }
        val removed = movieResults.size < initialSize
        if (removed) {
            saveAllMovieResults(movieResults)
        }
        return removed
    }
    
    /**
     * Gets a movie result by ID
     */
    fun getMovieResultById(id: String): MovieResult? {
        return loadMovieResults().firstOrNull { it.id == id }
    }
    
    /**
     * Gets a movie result by title (case-sensitive)
     */
    fun getMovieResultByTitle(title: String): MovieResult? {
        return loadMovieResults().firstOrNull { it.title == title }
    }
    
    /**
     * Clears all saved movie results
     */
    fun clearAllMovieResults() {
        saveAllMovieResults(emptyList())
    }
    
    /**
     * Saves all movie results to storage
     */
    private fun saveAllMovieResults(movieResults: List<MovieResult>) {
        try {
            val movieResultsList = MovieResultsList(movieResults)
            val data = json.encodeToString(movieResultsList)
            storageProvider.saveData(storageKey, data)
        } catch (e: Exception) {
            println("Error saving movies: ${e.message}")
        }
    }
}
