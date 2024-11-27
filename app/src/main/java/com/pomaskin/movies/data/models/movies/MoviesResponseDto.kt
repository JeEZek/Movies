package com.pomaskin.movies.data.models.movies

import com.google.gson.annotations.SerializedName

data class MoviesResponseDto (
    @SerializedName("page") val page: Int,
    @SerializedName("results") val videoList: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
)