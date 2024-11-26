package com.pomaskin.movies.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pomaskin.movies.navigation.AppNavGraph
import com.pomaskin.movies.navigation.Screen
import com.pomaskin.movies.navigation.rememberNavigationState
import com.pomaskin.movies.presentation.movie_single.MovieDescription
import com.pomaskin.movies.presentation.popular.PopularScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            BottomAppBar {
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()


                val items = listOf(
                    NavigationItem.Popular,
                    NavigationItem.Online,
                    NavigationItem.Favourite
                )
                items.forEach { item ->

                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false

                    NavigationBarItem(
                        selected = selected,
                        onClick = { if (!selected) navigationState.navigateTO(item.screen.route) },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(text = stringResource(id = item.titleResId)) }
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            popularScreenContent = {
                PopularScreen(
                    paddingValues = paddingValues,
                    onMovieCardClickListener = { movie ->
                        navigationState.navigateToMovieInfo(movie)
                    }
                )

            },
            movieSingleScreenContent = { movie ->
                MovieDescription(
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                    },
                    movie = movie
                )
            },
            onlineScreenContent = { TextCounter(name = "Online") },
            favouriteScreenContent = { TextCounter(name = "Favourite") }
        )
    }
}

@Composable
private fun TextCounter(name: String) {
    var count by rememberSaveable {
        mutableIntStateOf(0)
    }

    Text(
        modifier = Modifier.clickable { count++ },
        text = "$name Count: $count"
    )
}