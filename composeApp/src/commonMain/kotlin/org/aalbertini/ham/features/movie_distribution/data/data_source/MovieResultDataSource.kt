package org.aalbertini.ham.features.movie_distribution.data.data_source

import kotlinx.serialization.json.Json
import org.aalbertini.ham.core.data.storage.StorageProvider
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResultsList

/**
 * Data source for movie results storage operations
 * Handles low-level storage interactions
 */
class MovieResultDataSource(
    private val storageProvider: StorageProvider = StorageProvider()
) {
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    private val storageKey = "movies"
    
    /**
     * Loads all movie results from storage
     */
    fun loadAll(): List<MovieResult> {
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
     * Saves all movie results to storage
     */
    fun saveAll(movieResults: List<MovieResult>) {
        try {
            val movieResultsList = MovieResultsList(movieResults)
            val data = json.encodeToString(movieResultsList)
            storageProvider.saveData(storageKey, data)
        } catch (e: Exception) {
            println("Error saving movies: ${e.message}")
        }
    }
    
    /**
     * Clears all movie results from storage
     */
    fun clearAll() {
        saveAll(emptyList())
    }
}
