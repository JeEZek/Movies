package com.pomaskin.movies.data.models.video

import com.google.gson.annotations.SerializedName

data class VideoDto (
    @SerializedName("key") val key: String,
)