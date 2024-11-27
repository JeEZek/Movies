package com.pomaskin.movies.presentation.movie_single

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.domain.usecases.GetVideoUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieViewModel @Inject constructor(
    private val getVideoUseCase: GetVideoUseCase,
) : ViewModel() {

    suspend fun getVideo(movie: Movie): String {
        val videoKey = getVideoUseCase(movie.id).key
        return videoKey
    }
}