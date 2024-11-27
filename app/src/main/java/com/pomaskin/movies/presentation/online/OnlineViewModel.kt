package com.pomaskin.movies.presentation.online

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomaskin.movies.domain.usecases.LoadNextOnlineUseCase
import com.pomaskin.movies.domain.usecases.GetOnlineMoviesListUseCase
import com.pomaskin.movies.extensions.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnlineViewModel @Inject constructor(
    private val getOnlineMoviesListUseCase: GetOnlineMoviesListUseCase,
    private val loadNextOnlineUseCase: LoadNextOnlineUseCase,
) : ViewModel() {


    private val onlineFlow = getOnlineMoviesListUseCase()

    private val loadNextOnlineFlow = MutableSharedFlow<OnlineScreenState>()

    val screenState = onlineFlow
        .filter { it.isNotEmpty() }
        .map { OnlineScreenState.Movies(movies = it) as OnlineScreenState }
        .onStart { emit(OnlineScreenState.Loading) }
        .mergeWith(loadNextOnlineFlow)


    fun loadNextOnline() {
        Log.d("loadingNextOnline", "loadNextOnline")
        viewModelScope.launch {
            loadNextOnlineFlow.emit(
                OnlineScreenState.Movies(
                    movies = onlineFlow.value,
                    nextDataIsLoading = true
                )
            )
            loadNextOnlineUseCase()
        }
    }

}