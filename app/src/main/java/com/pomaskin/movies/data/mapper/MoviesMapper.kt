package com.pomaskin.movies.data.mapper

import com.pomaskin.movies.data.models.movies.MoviesResponseDto
import com.pomaskin.movies.data.models.video.VideoResponseDto
import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.domain.entity.Video
import javax.inject.Inject

class MoviesMapper @Inject constructor() {

    fun mapResponseToPosts(moviesResponseDto: MoviesResponseDto): List<Movie> {
        val result = mutableListOf<Movie>()

        val page = moviesResponseDto.page
        val videoList = moviesResponseDto.videoList
        val totalPages = moviesResponseDto.totalPages

        for (video in videoList) {
            val movie = Movie(
                id = video.id,
                posterPath = video.posterPath,
                title = video.title,
                overview = video.overview,
                voteAverage = video.voteAverage,
                voteCount = video.voteCount,
            )
            result.add(movie)
        }
        return result
    }

    fun mapVideoDtoToVideo(videoResponseDto: VideoResponseDto): Video {
        return Video(key = videoResponseDto.videoList[0].key)
    }
}