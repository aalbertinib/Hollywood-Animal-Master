package org.aalbertini.ham.di

import org.aalbertini.ham.features.movie_distribution.data.data_source.MovieResultDataSource
import org.aalbertini.ham.features.movie_distribution.data.repository.MovieResultRepository
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ClearAllMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.DeleteMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ExportLibraryUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.ImportLibraryUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.LoadMovieResultsUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.PlanDistributionUseCase
import org.aalbertini.ham.features.movie_distribution.domain.use_case.SaveMovieResultUseCase
import org.aalbertini.ham.features.movie_distribution.presentation.PlannerViewModel
import org.aalbertini.ham.features.settings.data.SettingsRepository
import org.aalbertini.ham.features.settings.presentation.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule: Module = module {
    single { MovieResultDataSource() }
    single { MovieResultRepository(get()) }
    single { SettingsRepository() }
}

val domainModule: Module = module {
    single { LoadMovieResultsUseCase(get()) }
    single { SaveMovieResultUseCase(get()) }
    single { DeleteMovieResultUseCase(get()) }
    single { ClearAllMovieResultsUseCase(get()) }
    single { ExportLibraryUseCase(get()) }
    single { ImportLibraryUseCase(get()) }
    single { PlanDistributionUseCase() }
}

val presentationModule: Module = module {
    viewModel {
        PlannerViewModel(
            loadMovies = get(),
            saveMovie = get(),
            deleteMovie = get(),
            clearMovies = get(),
            exportLibrary = get(),
            importLibrary = get(),
            planDistribution = get(),
        )
    }
    viewModel { SettingsViewModel(repository = get()) }
}

val appModules = listOf(dataModule, domainModule, presentationModule)
