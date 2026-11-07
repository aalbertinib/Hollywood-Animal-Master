package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository

/**
 * Use case for clearing all movie results
 */
class ClearAllMovieResultsUseCase(
    private val repository: MovieResultRepository
) {
    operator fun invoke() {
        repository.clearAllMovieResults()
    }
}
