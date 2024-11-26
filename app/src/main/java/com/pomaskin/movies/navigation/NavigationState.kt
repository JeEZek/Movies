package com.pomaskin.movies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pomaskin.movies.domain.entity.Movie

class NavigationState(
    val navHostController: NavHostController
) {

    fun navigateTO(route: String) {
        navHostController.navigate(route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    fun navigateToMovieInfo(movie: Movie) {
        navHostController.navigate(Screen.MovieInfo.getRouteWithArgs(movie))
    }
}




@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}
