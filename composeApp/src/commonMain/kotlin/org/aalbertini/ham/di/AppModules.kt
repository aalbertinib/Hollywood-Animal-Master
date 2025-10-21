package org.aalbertini.ham.di

import org.koin.dsl.module
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.aalbertini.ham.features.movie_distribution.data.data_source.MovieResultDataSource
import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository
import org.aalbertini.ham.features.movie_distribution.domain.use_case.CalculateWeeklyResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ClearAllMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.DeleteMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.GetMovieResultByIdUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.GetMovieResultByTitleUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.LoadMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.SaveMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.UpdateMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.parameters.ParametersViewModel
import org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.results.ResultsViewModel
import org.aalbertini.ham.features.movie_distribution.presentation.viewmodel.saved_movies.SavedMoviesViewModel

val dataModule: Module = module {
    single { MovieResultDataSource() }
    single { MovieResultRepository(get()) }
}

val domainModule: Module = module {
    single { CalculateWeeklyResultsUseCase() }
    single { LoadMovieResultsUseCase(get()) }
    single { SaveMovieResultUseCase(get()) }
    single { UpdateMovieResultUseCase(get()) }
    single { DeleteMovieResultUseCase(get()) }
    single { ClearAllMovieResultsUseCase(get()) }
    single { GetMovieResultByIdUseCase(get()) }
    single { GetMovieResultByTitleUseCase(get()) }
}

val presentationModule: Module = module {
    viewModel { ParametersViewModel() }
    viewModel { ResultsViewModel(calculateWeeklyResultsUseCase = get(), updateMovieResultUseCase = get()) }
    viewModel {
        SavedMoviesViewModel(
            loadMovieResultsUseCase = get(),
            saveMovieResultUseCase = get(),
            updateMovieResultUseCase = get(),
            deleteMovieResultUseCase = get(),
            clearAllMovieResultsUseCase = get(),
            getMovieResultByIdUseCase = get(),
            getMovieResultByTitleUseCase = get()
        )
    }
}

val appModules = listOf(dataModule, domainModule, presentationModule)
