package com.pomaskin.movies.data.repository

import com.pomaskin.movies.data.mapper.MoviesMapper
import com.pomaskin.movies.data.network.ApiFactory
import com.pomaskin.movies.data.network.ApiService
import com.pomaskin.movies.domain.entity.Movie
import com.pomaskin.movies.domain.repository.MoviesRepository
import com.pomaskin.movies.extensions.mergeWith
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

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<Movie>>()
    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            if (movies.isNotEmpty()) {
                emit(movies)
                return@collect
            }

            val response = apiService.loadPopularMovies(token, page++)

            val posts = mapper.mapResponseToPosts(response)
            _movies.addAll(posts)
            emit(movies)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    private val _movies = mutableListOf<Movie>()
    private val movies: List<Movie>
        get() = _movies.toList()


    private val recommendations: StateFlow<List<Movie>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = movies
        )


    override fun loadPopularMoviesList(): StateFlow<List<Movie>> {
        return recommendations
    }

    companion object {

        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }




}