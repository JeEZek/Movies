package com.pomaskin.movies.domain.repository

import com.pomaskin.movies.domain.entity.Movie
import kotlinx.coroutines.flow.StateFlow

interface MoviesRepository {

    fun loadPopularMoviesList(): StateFlow<List<Movie>>

    fun loadNowPlayingMoviesList(): StateFlow<List<Movie>>
}