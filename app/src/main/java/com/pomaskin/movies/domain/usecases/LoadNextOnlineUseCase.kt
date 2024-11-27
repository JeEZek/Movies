package com.pomaskin.movies.domain.usecases

import com.pomaskin.movies.domain.repository.MoviesRepository
import javax.inject.Inject

class LoadNextOnlineUseCase @Inject constructor(
    private val repository: MoviesRepository
) {

    suspend operator fun invoke() {
        repository.loadNextOnlineMoviesList()
    }
}