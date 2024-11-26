package com.pomaskin.movies.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomaskin.movies.domain.usecases.LoadPopularMoviesListUseCase
import com.pomaskin.movies.extensions.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularViewModel @Inject constructor(
    private val loadPopularMoviesListUseCase: LoadPopularMoviesListUseCase,
) : ViewModel() {

    private val popularFlow = loadPopularMoviesListUseCase()

    private val loadNextDataFlow = MutableSharedFlow<PopularScreenState>()

    val screenState = popularFlow
        .filter { it.isNotEmpty() }
        .map { PopularScreenState.Movies(movies = it) as PopularScreenState }
        .onStart { emit(PopularScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextPopular() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                PopularScreenState.Movies(
                    movies = popularFlow.value,
                    nextDataIsLoading = true
                )
            )
            loadPopularMoviesListUseCase()
        }
    }
}
