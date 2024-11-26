package com.pomaskin.movies.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation

import com.google.gson.Gson
import com.pomaskin.movies.domain.entity.Movie

fun NavGraphBuilder.homeScreenNavGraph(
    popularScreenContent: @Composable () -> Unit,
    movieSingleScreenContent: @Composable (Movie) -> Unit
) {
    navigation(
        startDestination = Screen.Popular.route,
        route = Screen.Popular.route
    ) {
        composable(route = Screen.Popular.route) {
            popularScreenContent()

        }
        composable(
            route = Screen.MovieInfo.route,
            arguments = listOf(
                navArgument(Screen.KEY_MOVIE) {
                    type = NavType.StringType
                }
            )
        ) {
            val movieJson = it.arguments?.getString(Screen.KEY_MOVIE) ?: ""
            val movie = Gson().fromJson(movieJson, Movie::class.java)
            movieSingleScreenContent(movie)
        }
    }
}