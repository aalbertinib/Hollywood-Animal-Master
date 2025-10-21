package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult

/**
 * Use case for getting a movie result by ID
 */
class GetMovieResultByIdUseCase(
    private val repository: MovieResultRepository
) {
    operator fun invoke(id: String): MovieResult? {
        return repository.getMovieResultById(id)
    }
}
