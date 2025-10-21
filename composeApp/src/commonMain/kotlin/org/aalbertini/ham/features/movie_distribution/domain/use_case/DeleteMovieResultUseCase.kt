package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository

/**
 * Use case for deleting a movie result
 */
class DeleteMovieResultUseCase(
    private val repository: MovieResultRepository
) {
    operator fun invoke(id: String): Boolean {
        return repository.deleteMovieResult(id)
    }
}
