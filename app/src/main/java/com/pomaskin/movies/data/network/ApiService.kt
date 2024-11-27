package com.pomaskin.movies.data.network

import com.pomaskin.movies.data.models.movies.MoviesResponseDto
import com.pomaskin.movies.data.models.video.VideoResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    data class FavoriteRequest(
        val media_type: String = "movie",
        val media_id: Int,
        val favorite: Boolean
    )

    @POST("account/{account_id}/favorite")
    suspend fun changeFavoriteStatus(
        @Path("account_id") accountId: Int,
        @Header("Authorization") authorization: String,
        @Body request: FavoriteRequest
    ): MoviesResponseDto

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
