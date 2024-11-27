package com.pomaskin.movies.domain.usecases

import com.pomaskin.movies.data.repository.MoviesRepositoryImpl
import com.pomaskin.movies.domain.entity.Video
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetVideoUseCase @Inject constructor(
    private val repository: MoviesRepositoryImpl
){

    suspend operator fun invoke(id: Int): Video {
        return repository.getVideo(id)
    }
}