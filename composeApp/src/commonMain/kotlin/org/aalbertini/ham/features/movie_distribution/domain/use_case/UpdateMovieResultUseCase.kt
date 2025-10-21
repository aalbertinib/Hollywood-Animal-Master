package org.aalbertini.ham.features.movie_distribution.domain.use_case

import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository

/**
 * Use case for updating an existing movie result
 */
class UpdateMovieResultUseCase(
    private val repository: MovieResultRepository
) {
    operator fun invoke(
        id: String,
        title: String,
        commercialScore: Double,
        numberOfScreenings: Double,
        availableScreeningsOverrides: Map<Int, Double> = emptyMap(),
        weekMultiplierOverrides: Map<Int, Double> = emptyMap()
    ): Boolean {
        return repository.updateMovieResult(
            id = id,
            title = title,
            commercialScore = commercialScore,
            numberOfScreenings = numberOfScreenings,
            availableScreeningsOverrides = availableScreeningsOverrides,
            weekMultiplierOverrides = weekMultiplierOverrides
        )
    }
}
