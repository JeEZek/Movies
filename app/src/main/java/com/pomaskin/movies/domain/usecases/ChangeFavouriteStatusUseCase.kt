package com.pomaskin.movies.domain.usecases

import com.pomaskin.movies.data.repository.MoviesRepositoryImpl
import javax.inject.Inject

class ChangeFavouriteStatusUseCase @Inject constructor(
    private val repository: MoviesRepositoryImpl
) {
    suspend operator fun invoke(mediaId: Int, favorite: Boolean) {
        return repository.changeFavouriteStatus(
            mediaId = mediaId,
            favorite = favorite
        )
    }
}