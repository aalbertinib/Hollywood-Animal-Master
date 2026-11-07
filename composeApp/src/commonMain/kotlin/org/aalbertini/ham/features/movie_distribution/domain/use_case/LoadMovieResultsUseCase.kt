package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult

/**
 * Use case for loading all movie results
 */
class LoadMovieResultsUseCase(
    private val repository: MovieResultRepository
) {
    operator fun invoke(): List<MovieResult> {
        return repository.loadMovieResults()
    }
}
