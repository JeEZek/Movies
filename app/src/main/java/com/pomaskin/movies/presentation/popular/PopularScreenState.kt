package com.pomaskin.movies.presentation.popular

import com.pomaskin.movies.domain.entity.Movie

sealed class PopularScreenState {

    object Initial : PopularScreenState()

    object Loading : PopularScreenState()

    data class Movies(
        val movies: List<Movie>,
        val nextDataIsLoading: Boolean = false
    ) : PopularScreenState()
}