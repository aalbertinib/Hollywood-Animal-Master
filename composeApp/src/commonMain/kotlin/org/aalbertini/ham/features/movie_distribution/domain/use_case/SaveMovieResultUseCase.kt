package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult

/**
 * Use case for saving a movie: creates it when its id is blank or unknown, replaces it otherwise
 */
class SaveMovieResultUseCase(
    private val repository: MovieResultRepository
) {
    operator fun invoke(movie: MovieResult): MovieResult = repository.saveMovieResult(movie)
}
