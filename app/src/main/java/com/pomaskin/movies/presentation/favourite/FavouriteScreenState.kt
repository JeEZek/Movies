package com.pomaskin.movies.presentation.favourite

import com.pomaskin.movies.domain.entity.Movie

sealed class FavouriteScreenState {

    object Initial : FavouriteScreenState()

    object Loading : FavouriteScreenState()

    data class Movies(
        val movies: List<Movie>,
        val nextDataIsLoading: Boolean = false
    ) : FavouriteScreenState()
}