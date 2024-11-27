package com.pomaskin.movies.presentation.online

import com.pomaskin.movies.domain.entity.Movie

sealed class OnlineScreenState {

    object Initial : OnlineScreenState()

    object Loading : OnlineScreenState()

    data class Movies(
        val movies: List<Movie>,
        val nextDataIsLoading: Boolean = false
    ) : OnlineScreenState()
}