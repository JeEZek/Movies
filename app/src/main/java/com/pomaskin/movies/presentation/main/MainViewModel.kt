package com.pomaskin.movies.presentation.main

import androidx.lifecycle.ViewModel
import com.pomaskin.movies.domain.usecases.GetPopularMoviesListUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getPopularMoviesListUseCase: GetPopularMoviesListUseCase
): ViewModel() {
}