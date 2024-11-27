package com.pomaskin.movies.data.network

import com.pomaskin.movies.data.models.movies.MoviesResponseDto
import com.pomaskin.movies.data.models.video.VideoResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/{type}")
    suspend fun getPopularMovies(
        @Path("type") type: String,
        @Query("api_key") token: String,
        @Query("page") page: Int = 1
    ): MoviesResponseDto


    @GET("movie/{movie_id}/videos")
    suspend fun getVideo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") token: String,
    ): VideoResponseDto
}
