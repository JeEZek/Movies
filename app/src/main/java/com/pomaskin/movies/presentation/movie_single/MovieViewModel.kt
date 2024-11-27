package com.pomaskin.movies.presentation.movie_single

import androidx.lifecycle.ViewModel
import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.domain.usecases.ChangeFavouriteStatusUseCase
import com.pomaskin.movies.domain.usecases.GetVideoUseCase
import javax.inject.Inject

class MovieViewModel @Inject constructor(
    private val getVideoUseCase: GetVideoUseCase,
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase
) : ViewModel() {

    suspend fun getVideo(movie: Movie): String {
        return getVideoUseCase(movie.id).key
    }

    suspend fun changeFavouriteStatus(
        mediaId: Int,
        favorite: Boolean,
    ) {
        return changeFavouriteStatusUseCase(
            mediaId = mediaId,
            favorite = favorite
        )
    }
}