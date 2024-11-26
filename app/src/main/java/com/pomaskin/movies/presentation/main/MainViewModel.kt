package com.pomaskin.movies.presentation.main

import androidx.lifecycle.ViewModel
import com.pomaskin.movies.domain.usecases.LoadPopularMoviesListUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val loadPopularMoviesListUseCase: LoadPopularMoviesListUseCase
): ViewModel() {
}