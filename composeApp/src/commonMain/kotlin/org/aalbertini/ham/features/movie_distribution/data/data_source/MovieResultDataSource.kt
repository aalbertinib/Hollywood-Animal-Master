package org.aalbertini.ham.features.movie_distribution.data.data_source

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import org.aalbertini.ham.core.data.storage.KeyValueStore
import org.aalbertini.ham.core.data.storage.StorageProvider
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResultsList

/**
 * Reads and writes the saved movies as one JSON document.
 *
 * Every save rewrites the whole list, so a document that fails to decode must never turn into
 * an empty list that the next save writes back (V1 did that: one bad entry wiped the library).
 * Each movie is decoded on its own, and when anything can't be read the raw document is copied
 * to [BACKUP_KEY] before it can be overwritten.
 */
class MovieResultDataSource(
    private val storage: KeyValueStore = StorageProvider()
) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun loadAll(): List<MovieResult> {
        val data = storage.loadData(STORAGE_KEY)?.takeIf { it.isNotBlank() } ?: return emptyList()
        val decoded = decode(data)
        if (decoded == null || decoded.skipped > 0) backup(data)
        return decoded?.movies.orEmpty()
    }

    /** The library as a JSON document, in the same format as storage (for backups between devices). */
    fun encode(movieResults: List<MovieResult>): String = json.encodeToString(MovieResultsList(movieResults))

    /**
     * Reads a library document. Unreadable entries are skipped and counted.
     *
     * An empty library is stored as `{}` (defaults aren't encoded), so a missing list means empty
     * unless [requireList] is set, as for pasted text that has to look like an export.
     * @return `null` when [data] isn't a library document at all.
     */
    fun decode(data: String, requireList: Boolean = false): Decoded? {
        val document = runCatching { json.parseToJsonElement(data.trim()).jsonObject }.getOrNull() ?: return null
        val entries = when (val list = document[LIST_FIELD]) {
            null -> if (requireList) return null else JsonArray(emptyList())
            is JsonArray -> list
            else -> return null
        }
        val movies = entries.mapNotNull { entry ->
            runCatching { json.decodeFromJsonElement(MovieResult.serializer(), entry) }
                .onFailure { println("Skipping unreadable movie: ${it.message}") }
                .getOrNull()
        }
        return Decoded(movies, skipped = entries.size - movies.size)
    }

    data class Decoded(val movies: List<MovieResult>, val skipped: Int)

    fun saveAll(movieResults: List<MovieResult>) {
        try {
            storage.saveData(STORAGE_KEY, encode(movieResults))
        } catch (e: Exception) {
            println("Error saving movies: ${e.message}")
        }
    }

    fun clearAll() {
        saveAll(emptyList())
    }

    /** Copies [data] aside, skipping the write when the backup already holds it. */
    private fun backup(data: String) {
        if (storage.loadData(BACKUP_KEY) != data) storage.saveData(BACKUP_KEY, data)
    }

    companion object {
        const val STORAGE_KEY = "movies"
        const val BACKUP_KEY = "movies_unreadable_backup"
        private const val LIST_FIELD = "movieResults"
    }
}
