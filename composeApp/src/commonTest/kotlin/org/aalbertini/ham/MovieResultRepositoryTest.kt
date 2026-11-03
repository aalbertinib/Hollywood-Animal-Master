package org.aalbertini.ham

import org.aalbertini.ham.features.movie_distribution.data.data_source.MovieResultDataSource
import org.aalbertini.ham.features.movie_distribution.data.repository.ImportResult
import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository
import org.aalbertini.ham.features.movie_distribution.domain.model.DistributionMethod
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MovieResultRepositoryTest {

    private val store = InMemoryStore()
    private val repository = MovieResultRepository(MovieResultDataSource(store))

    private fun movie(title: String, id: String = "") =
        MovieResult(id = id, title = title, commercialScore = 5.0, numberOfScreenings = 3200.0)

    @Test
    fun save_newMovie_getsIdAndTimestamps() {
        val stored = repository.saveMovieResult(movie("A"))
        assertTrue(stored.id.isNotBlank())
        assertTrue(stored.createdAt > 0)
        assertEquals(listOf(stored), repository.loadMovieResults())
    }

    @Test
    fun save_existingId_replacesAndKeepsCreatedAt() {
        val first = repository.saveMovieResult(movie("A"))
        val second = repository.saveMovieResult(first.copy(title = "B", createdAt = 1))
        assertEquals(first.id, second.id)
        assertEquals(first.createdAt, second.createdAt)
        assertEquals(listOf("B"), repository.loadMovieResults().map { it.title })
    }

    @Test
    fun delete_removesOnlyThatMovie() {
        val a = repository.saveMovieResult(movie("A"))
        repository.saveMovieResult(movie("B"))
        assertTrue(repository.deleteMovieResult(a.id))
        assertEquals(listOf("B"), repository.loadMovieResults().map { it.title })
    }

    @Test
    fun unreadableDocument_isBackedUp_andNeverOverwrittenSilently() {
        store.data[MovieResultDataSource.STORAGE_KEY] = "{ this is not json"
        assertEquals(emptyList(), repository.loadMovieResults())
        // The next save rewrites "movies", but the original text survives in the backup.
        repository.saveMovieResult(movie("New"))
        assertEquals("{ this is not json", store.data[MovieResultDataSource.BACKUP_KEY])
    }

    @Test
    fun clearedLibrary_isEmpty_notUnreadable() {
        repository.saveMovieResult(movie("A"))
        repository.clearAllMovieResults()
        assertEquals("{}", store.data[MovieResultDataSource.STORAGE_KEY]?.filterNot { it.isWhitespace() })
        assertEquals(emptyList(), repository.loadMovieResults())
        assertNull(store.data[MovieResultDataSource.BACKUP_KEY])
    }

    @Test
    fun oneBadEntry_keepsTheOthers() {
        store.data[MovieResultDataSource.STORAGE_KEY] = """
            {"movieResults": [
                {"id": "1", "title": "Good", "commercialScore": 5.0, "numberOfScreenings": 3200.0},
                {"id": "2", "title": "Bad", "commercialScore": "not a number"}
            ]}
        """.trimIndent()
        assertEquals(listOf("Good"), repository.loadMovieResults().map { it.title })
        assertTrue(store.data.containsKey(MovieResultDataSource.BACKUP_KEY))
    }

    @Test
    fun v1Document_decodesWithV1Method() {
        store.data[MovieResultDataSource.STORAGE_KEY] = """
            {"movieResults": [{"id": "v1", "title": "Old", "commercialScore": 6.5, "numberOfScreenings": 3200.0,
              "availableScreeningsOverrides": {"2": 1000.0}}]}
        """.trimIndent()
        val loaded = repository.loadMovieResults().single()
        assertEquals(DistributionMethod.STEAM_GUIDE, loaded.method)
        assertEquals(mapOf(2 to 1000.0), loaded.availableScreeningsOverrides)
        assertNull(store.data[MovieResultDataSource.BACKUP_KEY])
    }

    @Test
    fun exportThenImport_intoEmptyLibrary_copiesEverything() {
        repository.saveMovieResult(movie("A"))
        repository.saveMovieResult(movie("B"))
        val other = MovieResultRepository(MovieResultDataSource(InMemoryStore()))
        val result = other.importLibrary(repository.exportLibrary())
        assertEquals(ImportResult(added = 2, updated = 0, unchanged = 0, unreadable = 0), result)
        assertEquals(repository.loadMovieResults(), other.loadMovieResults())
    }

    @Test
    fun import_sameMovie_newestCopyWins() {
        val here = repository.saveMovieResult(movie("Here"))
        val older = here.copy(title = "Older", updatedAt = here.updatedAt - 1)
        val newer = here.copy(title = "Newer", updatedAt = here.updatedAt + 1)
        val export = MovieResultDataSource(InMemoryStore())

        assertEquals(1, repository.importLibrary(export.encode(listOf(older)))?.unchanged)
        assertEquals("Here", repository.loadMovieResults().single().title)

        assertEquals(1, repository.importLibrary(export.encode(listOf(newer)))?.updated)
        assertEquals("Newer", repository.loadMovieResults().single().title)
    }

    @Test
    fun import_movieWithoutId_getsOne() {
        val text = MovieResultDataSource(InMemoryStore()).encode(listOf(movie("No id")))
        assertEquals(1, repository.importLibrary(text)?.added)
        assertNotEquals("", repository.loadMovieResults().single().id)
    }

    @Test
    fun import_text_thatIsNotAnExport_returnsNull_andChangesNothing() {
        repository.saveMovieResult(movie("A"))
        val before = store.data.toMap()
        assertNull(repository.importLibrary("hello"))
        assertNull(repository.importLibrary("""{"something": 1}"""))
        assertEquals(before, store.data)
    }
}
