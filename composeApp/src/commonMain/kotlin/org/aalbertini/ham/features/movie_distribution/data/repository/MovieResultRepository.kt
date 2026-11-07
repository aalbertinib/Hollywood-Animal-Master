package org.aalbertini.ham.features.movie_distribution.data.repository

import org.aalbertini.ham.features.movie_distribution.data.data_source.MovieResultDataSource
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Repository for managing saved movies.
 * Saving is an upsert of the whole [MovieResult], so new fields never need new parameters here.
 */
@OptIn(ExperimentalTime::class)
class MovieResultRepository(
    private val dataSource: MovieResultDataSource = MovieResultDataSource()
) {

    /** All saved movies, most recently updated first. */
    fun loadMovieResults(): List<MovieResult> =
        dataSource.loadAll().sortedByDescending { it.updatedAt }

    /**
     * Inserts [movie] (blank id → new id) or replaces the saved movie with the same id.
     * @return the stored movie, with its id and timestamps set.
     */
    fun saveMovieResult(movie: MovieResult): MovieResult {
        val movieResults = dataSource.loadAll().toMutableList()
        val now = Clock.System.now().toEpochMilliseconds()
        val index = movieResults.indexOfFirst { it.id == movie.id }
        val stored = if (index == -1) {
            movie.copy(
                id = movie.id.ifBlank { generateId(now) },
                // Keeps the original date when an undo puts a deleted movie back.
                createdAt = movie.createdAt.takeIf { it > 0 } ?: now,
                updatedAt = now,
            ).also { movieResults += it }
        } else {
            movie.copy(createdAt = movieResults[index].createdAt, updatedAt = now)
                .also { movieResults[index] = it }
        }
        dataSource.saveAll(movieResults)
        return stored
    }

    /** Every saved movie as a JSON document that [importLibrary] reads back. */
    fun exportLibrary(): String = dataSource.encode(loadMovieResults())

    /**
     * Merges an exported library into this one. A movie whose id is already here replaces it only
     * when the imported copy was updated more recently; movies without an id get a new one.
     * @return what changed, or `null` when [text] is not an exported library.
     */
    fun importLibrary(text: String): ImportResult? {
        val decoded = dataSource.decode(text, requireList = true) ?: return null
        val movieResults = dataSource.loadAll().toMutableList()
        val now = Clock.System.now().toEpochMilliseconds()
        var added = 0
        var updated = 0
        var unchanged = 0
        decoded.movies.forEach { movie ->
            val index = movieResults.indexOfFirst { it.id == movie.id && movie.id.isNotBlank() }
            when {
                index == -1 -> {
                    movieResults += movie.copy(
                        id = movie.id.ifBlank { generateId(now) },
                        createdAt = movie.createdAt.takeIf { it > 0 } ?: now,
                        updatedAt = movie.updatedAt.takeIf { it > 0 } ?: now,
                    )
                    added++
                }
                movie.updatedAt > movieResults[index].updatedAt -> {
                    movieResults[index] = movie
                    updated++
                }
                else -> unchanged++
            }
        }
        if (added + updated > 0) dataSource.saveAll(movieResults)
        return ImportResult(added = added, updated = updated, unchanged = unchanged, unreadable = decoded.skipped)
    }

    /**
     * Deletes a movie result
     */
    fun deleteMovieResult(id: String): Boolean {
        val movieResults = dataSource.loadAll().toMutableList()
        val removed = movieResults.removeAll { it.id == id }
        if (removed) {
            dataSource.saveAll(movieResults)
        }
        return removed
    }

    /**
     * Clears all saved movie results
     */
    fun clearAllMovieResults() {
        dataSource.clearAll()
    }

    private fun generateId(now: Long): String = "$now-${Random.nextInt(10000, 99999)}"
}

/** Outcome of [MovieResultRepository.importLibrary]. */
data class ImportResult(val added: Int, val updated: Int, val unchanged: Int, val unreadable: Int)
