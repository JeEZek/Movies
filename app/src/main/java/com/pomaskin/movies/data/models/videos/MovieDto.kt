package com.pomaskin.movies.data.models.videos

import com.google.gson.annotations.SerializedName

data class MovieDto (
    @SerializedName("id") val id: Int,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("title") val title: String,
)