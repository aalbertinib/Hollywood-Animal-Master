package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository
import org.aalbertini.ham.features.movie_distribution.domain.model.MovieResult

/**
 * Use case for saving a new movie result
 */
class SaveMovieResultUseCase(
    private val repository: MovieResultRepository
) {
    operator fun invoke(
        title: String,
        commercialScore: Double,
        numberOfScreenings: Double,
        availableScreeningsOverrides: Map<Int, Double> = emptyMap(),
        weekMultiplierOverrides: Map<Int, Double> = emptyMap()
    ): MovieResult {
        return repository.saveMovieResult(
            title = title,
            commercialScore = commercialScore,
            numberOfScreenings = numberOfScreenings,
            availableScreeningsOverrides = availableScreeningsOverrides,
            weekMultiplierOverrides = weekMultiplierOverrides
        )
    }
}
