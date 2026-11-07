package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository

/**
 * Use case for exporting every saved movie as JSON text (backup, or moving to another device)
 */
class ExportLibraryUseCase(
    private val repository: MovieResultRepository
) {
    operator fun invoke(): String = repository.exportLibrary()
}
