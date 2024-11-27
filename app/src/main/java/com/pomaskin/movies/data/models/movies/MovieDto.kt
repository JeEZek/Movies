package com.pomaskin.movies.data.models.movies

import com.google.gson.annotations.SerializedName

data class MovieDto (
    @SerializedName("id") val id: Int,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val voteAverage: String,
    @SerializedName("vote_count") val voteCount: String,
)