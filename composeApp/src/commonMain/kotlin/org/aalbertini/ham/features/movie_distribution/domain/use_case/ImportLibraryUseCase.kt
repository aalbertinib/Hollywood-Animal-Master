package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.data.repository.ImportResult
import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository

/**
 * Use case for merging exported JSON text into the library; `null` when the text isn't an export
 */
class ImportLibraryUseCase(
    private val repository: MovieResultRepository
) {
    operator fun invoke(text: String): ImportResult? = repository.importLibrary(text)
}
