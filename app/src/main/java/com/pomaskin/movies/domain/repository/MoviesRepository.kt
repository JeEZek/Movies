package com.pomaskin.movies.domain.repository

import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.domain.entity.Video
import kotlinx.coroutines.flow.StateFlow

interface MoviesRepository {

    fun getPopularMoviesList(): StateFlow<List<Movie>>

    suspend fun getVideo(movieId: Int): Video

    suspend fun loadNextPopularMoviesList()
}