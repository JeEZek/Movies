package com.pomaskin.movies.data.network

import com.pomaskin.movies.data.models.videos.ResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun loadPopularMovies(
        @Query("api_key") token: String,
        @Query("page") page: Int = 1
    ): ResponseDto


    @GET("movie/now_playing")
    suspend fun loadNowPlayingMovies(
        @Query("api_key") token: String,
        @Query("page") page: Int = 1
    )
}
