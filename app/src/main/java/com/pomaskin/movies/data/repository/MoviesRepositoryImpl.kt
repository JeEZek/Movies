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
    private val accountId = 21651574
    private val authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjMGEwZWFiYjBkMjVkZTJjMjBkZGE1YWM2YWYwZWIxMCIsIm5iZiI6MTczMjYwNzY0My4xMTk0Mjg2LCJzdWIiOiI2NzQ0NzcwZGMyNDc2NWZhMmYyZGU3YTYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.2KQEvwMt7OnQ-cZv-s97rltkx6Zm0QjbWdo6dApIgNQ"


    // TODO new data is loading, but now come to UI -_-  FIX BUG
    // TODO able to change type and use same functions for popular and online pages
    // TODO after re-opening favourite page or when press on favourite button load data again
    // TODO delete from favourite
    // TODO exception handlers
    // TODO combine Popular and Online page functions to 1 with param type(popular, online) for ApiService

//
//
//    For Popular page
//
//
    private var pagePopular = 1

    //cashed data
    private val _movies = mutableListOf<Movie>()
    private val movies: List<Movie>
        get() = _movies.toList()


    //loading data
    private val popularCoroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)

    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            Log.d("loadingNextPopular", "nextDataNeededEvents IMPL")
            val response = apiService.getPopularMovies(token = token, page = pagePopular++)
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
            scope = popularCoroutineScope,
            started = SharingStarted.Lazily,
            initialValue = movies
        )

    override fun getPopularMoviesList(): StateFlow<List<Movie>> {
        Log.d("loadingNextPopular", "${recommendations.value}")
        return recommendations
    }

    override suspend fun loadNextPopularMoviesList() {
        nextDataNeededEvents.emit(Unit)
    }




//
//
//    For Favourite page
//
//
    private val favouriteCoroutineScope = CoroutineScope(Dispatchers.Default)

    private var pageOnline = 1

    //cashed data
    private val _moviesOnline = mutableListOf<Movie>()
    private val moviesOnline: List<Movie>
        get() = _moviesOnline.toList()

    //loading data
    private val coroutineScopeOnline = CoroutineScope(Dispatchers.Default)
    private val nextOnlineDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val loadedOnlineListFlow = flow {
        nextOnlineDataNeededEvents.emit(Unit)
        nextOnlineDataNeededEvents.collect {
            Log.d("loadingNextOnline", "nextDataNeededEvents IMPL")
            val response = apiService.getOnlineMovies(token = token, page = pageOnline)
            Log.d("loadingNextOnline", "response $response")
            val posts = mapper.mapResponseToPosts(response)
            _moviesOnline.addAll(posts)
            emit(moviesOnline)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }


    private val recommendationsOnline: StateFlow<List<Movie>> = loadedOnlineListFlow
        .stateIn(
            scope = favouriteCoroutineScope,
            started = SharingStarted.Lazily,
            initialValue = moviesOnline
        )

    override fun getOnlineMoviesList(): StateFlow<List<Movie>> {
        Log.d("loadingNextOnline", "${recommendationsOnline.value}")
        return recommendationsOnline
    }

    override suspend fun loadNextOnlineMoviesList() {
        nextOnlineDataNeededEvents.emit(Unit)
    }






//
//
//    For Favourite page
//
//
    private var pageFavourite = 1

    //cashed data
    private val _moviesFavourite = mutableListOf<Movie>()
    private val moviesFavourite: List<Movie>
        get() = _moviesFavourite.toList()

    //loading data
    private val favouriteScopeOnline = CoroutineScope(Dispatchers.Default)
    private val nextFavouriteDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val loadedFavouriteListFlow = flow {
        nextFavouriteDataNeededEvents.emit(Unit)
        nextFavouriteDataNeededEvents.collect {
            Log.d("loadingNextFavourite", "nextDataNeededEvents IMPL")
            val response = apiService.getFavouriteMovies(accountId = accountId, authorization = authorization, page = pageFavourite)
            Log.d("loadingNextPopular", "response $response")
            val posts = mapper.mapResponseToPosts(response)
            _moviesFavourite.addAll(posts)
            emit(moviesFavourite)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }


    private val recommendationsFavourite: StateFlow<List<Movie>> = loadedFavouriteListFlow
        .stateIn(
            scope = favouriteScopeOnline,
            started = SharingStarted.Lazily,
            initialValue = moviesFavourite
        )

    override fun getFavouriteMoviesList(): StateFlow<List<Movie>> {
        Log.d("loadingNextFavourite", "${recommendationsFavourite.value}")
        return recommendationsFavourite
    }

    override suspend fun loadNextFavouriteMoviesList() {
        nextFavouriteDataNeededEvents.emit(Unit)
    }










    override suspend fun getVideo(movieId: Int): Video {
        return mapper.mapVideoDtoToVideo(apiService.getVideo(movieId, token))
    }



    override suspend fun changeFavouriteStatus(
        mediaId: Int,
        favorite: Boolean
    ) {
        apiService.changeFavoriteStatus(
            accountId = accountId,
            authorization = authorization,
            request = ApiService.FavoriteRequest(media_id = mediaId, favorite = favorite)
        )
    }


    companion object {

        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}