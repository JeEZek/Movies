package com.pomaskin.movies.data.models.videos

import com.google.gson.annotations.SerializedName

data class ResponseDto (
    @SerializedName("page") val page: Int,
    @SerializedName("results") val videoList: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
)