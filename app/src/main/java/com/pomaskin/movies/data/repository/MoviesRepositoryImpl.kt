package com.pomaskin.movies.data.repository

import android.util.Log
import com.pomaskin.movies.data.mapper.MoviesMapper
import com.pomaskin.movies.data.network.ApiService
import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.domain.entity.Video
import com.pomaskin.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: MoviesMapper,
) : MoviesRepository {

    private val token = "c0a0eabb0d25de2c20dda5ac6af0eb10"
    private var page = 1

    //cashed data
    private val _movies = mutableListOf<Movie>()
    private val movies: List<Movie>
        get() = _movies.toList()


    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    // TODO new data is not showing
    //loading data
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            Log.d("loadingNextPopular", "nextDataNeededEvents IMPL")
            val response = apiService.getPopularMovies(type = "popular", token = token, page = page++)
            Log.d("loadingNextPopular", "response $response")
            val posts = mapper.mapResponseToPosts(response)
            _movies.addAll(posts)
            emit(movies)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }


    private val recommendations: StateFlow<List<Movie>> = loadedListFlow
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = movies
        )
    override fun getPopularMoviesList(): StateFlow<List<Movie>> {
        Log.d("loadingNextPopular", "${recommendations.value}")
        return recommendations
    }

    override suspend fun getVideo(movieId: Int): Video {
        return mapper.mapVideoDtoToVideo(apiService.getVideo(movieId, token))
    }

    override suspend fun loadNextPopularMoviesList() {
        nextDataNeededEvents.emit(Unit)
    }



    companion object {

        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}