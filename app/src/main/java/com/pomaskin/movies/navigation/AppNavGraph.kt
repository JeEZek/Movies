package com.pomaskin.movies.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pomaskin.movies.domain.entity.Movie

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    popularScreenContent: @Composable () -> Unit,
    movieSingleScreenContent: @Composable (Movie) -> Unit,
    onlineScreenContent: @Composable () -> Unit,
    favouriteScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        homeScreenNavGraph(
            popularScreenContent = popularScreenContent,
            movieSingleScreenContent = movieSingleScreenContent
        )
        composable(route = Screen.Online.route) {
            onlineScreenContent()
        }
        composable(route = Screen.Favourite.route) {
            favouriteScreenContent()
        }
    }
}