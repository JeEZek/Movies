package com.pomaskin.movies.data.models.video

import com.google.gson.annotations.SerializedName

data class VideoResponseDto (
    @SerializedName("results") val videoList: List<VideoDto>,
)