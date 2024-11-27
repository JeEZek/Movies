package com.pomaskin.movies.presentation.popular

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomaskin.movies.domain.usecases.LoadNextPopularUseCase
import com.pomaskin.movies.domain.usecases.GetPopularMoviesListUseCase
import com.pomaskin.movies.extensions.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularViewModel @Inject constructor(
    private val getPopularMoviesListUseCase: GetPopularMoviesListUseCase,
    private val loadNextPopularUseCase: LoadNextPopularUseCase,
) : ViewModel() {


    private val popularFlow = getPopularMoviesListUseCase()

    private val loadNextPopularFlow = MutableSharedFlow<PopularScreenState>()

    val screenState = popularFlow
        .filter { it.isNotEmpty() }
        .map { PopularScreenState.Movies(movies = it) as PopularScreenState }
        .onStart { emit(PopularScreenState.Loading) }
        .mergeWith(loadNextPopularFlow)


    fun loadNextPopular() {
        Log.d("loadingNextPopular", "loadNextPopular")
        viewModelScope.launch {
            loadNextPopularFlow.emit(
                PopularScreenState.Movies(
                    movies = popularFlow.value,
                    nextDataIsLoading = true
                )
            )
            loadNextPopularUseCase()
        }
    }

}