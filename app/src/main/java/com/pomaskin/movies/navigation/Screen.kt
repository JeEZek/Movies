package com.pomaskin.movies.navigation

import android.net.Uri
import com.google.gson.Gson
import com.pomaskin.movies.domain.entity.Movie

sealed class Screen(
    val route: String
) {

    object Home: Screen(ROUTE_HOME)

    object Popular: Screen(ROUTE_POPULAR)

    object MovieInfo: Screen(ROUTE_MOVIE) {

        private const val ROUTE_FOR_ARGS = "movies"

        fun getRouteWithArgs(movie: Movie): String {
            val movieJson = Gson().toJson(movie)
            return "$ROUTE_FOR_ARGS/${movieJson.encode()}"
        }
    }

    object Online: Screen(ROUTE_ONLINE)
    object Favourite: Screen(ROUTE_FAVOURITE)


    companion object {
        const val KEY_MOVIE = "movie"

        const val ROUTE_HOME = "home"
        const val ROUTE_MOVIE = "movies/{$KEY_MOVIE}"
        const val ROUTE_POPULAR = "popular"
        const val ROUTE_ONLINE = "online"
        const val ROUTE_FAVOURITE = "favourite"

    }
}

fun String.encode():String {
    return Uri.encode(this)
}