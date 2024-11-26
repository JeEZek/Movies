package com.pomaskin.movies.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class Movie(
    val id: Int,
    val posterPath: String,
    val title: String
)