package com.pomaskin.movies.presentation.favourite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomaskin.movies.domain.usecases.GetFavouriteMoviesListUseCase
import com.pomaskin.movies.domain.usecases.LoadNextFavouriteUseCase
import com.pomaskin.movies.extensions.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteViewModel @Inject constructor(
    private val getFavouriteMoviesListUseCase: GetFavouriteMoviesListUseCase,
    private val loadNextFavouriteUseCase: LoadNextFavouriteUseCase,
) : ViewModel() {


    private val favouriteFlow = getFavouriteMoviesListUseCase()

    private val loadNextFavouriteEvent = MutableSharedFlow<Unit>()
    private val loadNextFavouriteFlow = flow {
        loadNextFavouriteEvent.collect{
            Log.d("loadingNextFavourite", "loadNextFavouriteEvent. movies in var - ${favouriteFlow.value}")
            emit(
                FavouriteScreenState.Movies(
                    movies = favouriteFlow.value,
                    nextDataIsLoading = true
                )
            )
        }
    }

    val screenState = favouriteFlow
        .filter { it.isNotEmpty() }
        .map { FavouriteScreenState.Movies(movies = it) as FavouriteScreenState }
        .onStart { emit(FavouriteScreenState.Loading) }
        .mergeWith(loadNextFavouriteFlow)

    fun loadNextFavourite() {

        Log.d("loadingNextFavourite", "loadNextFavourite")
        viewModelScope.launch {
            loadNextFavouriteEvent.emit(Unit)
            loadNextFavouriteUseCase()
        }
    }
}