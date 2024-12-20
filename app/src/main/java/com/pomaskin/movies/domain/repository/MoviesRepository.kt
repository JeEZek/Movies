package com.pomaskin.movies.domain.repository

import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.domain.entity.Video
import kotlinx.coroutines.flow.StateFlow

interface MoviesRepository {

    fun getPopularMoviesList(): StateFlow<List<Movie>>

    fun getFavouriteMoviesList(): StateFlow<List<Movie>>

    fun getOnlineMoviesList(): StateFlow<List<Movie>>


    suspend fun getVideo(movieId: Int): Video

    suspend fun loadNextPopularMoviesList()

    suspend fun loadNextFavouriteMoviesList()

    suspend fun loadNextOnlineMoviesList()

    suspend fun changeFavouriteStatus(mediaId: Int, favorite: Boolean)
}