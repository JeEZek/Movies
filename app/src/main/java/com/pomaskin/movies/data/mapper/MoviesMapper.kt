package com.pomaskin.movies.data.mapper

import com.pomaskin.movies.data.models.videos.ResponseDto
import com.pomaskin.movies.domain.entity.Movie
import javax.inject.Inject

class MoviesMapper @Inject constructor() {

    fun mapResponseToPosts(responseDto: ResponseDto): List<Movie> {
        val result = mutableListOf<Movie>()

        val page = responseDto.page
        val videoList = responseDto.videoList
        val totalPages = responseDto.totalPages

        for (video in videoList) {
            val movie = Movie(
                id = video.id,
                posterPath = video.posterPath,
                title = video.title
            )
            result.add(movie)
        }
        return result
    }


}