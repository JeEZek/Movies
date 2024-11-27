package com.pomaskin.movies.domain.usecases

import com.pomaskin.movies.data.repository.MoviesRepositoryImpl
import com.pomaskin.movies.domain.entity.Movie

import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetPopularMoviesListUseCase @Inject constructor(
    private val repository: MoviesRepositoryImpl
){

    operator fun invoke(): StateFlow<List<Movie>> {
        return repository.getPopularMoviesList()
    }
}