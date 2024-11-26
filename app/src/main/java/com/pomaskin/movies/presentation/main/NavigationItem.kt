package com.pomaskin.movies.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.pomaskin.movies.R
import com.pomaskin.movies.navigation.Screen


sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector
) {

    object Popular : NavigationItem(
        screen = Screen.Home,
        titleResId = R.string.navigation_item_popular,
        icon = Icons.Outlined.Home
    )

    object  Online: NavigationItem(
        screen = Screen.Favourite,
        titleResId = R.string.navigation_item_online,
        icon = Icons.Outlined.Favorite
    )

    object Favourite : NavigationItem(
        screen = Screen.Online,
        titleResId = R.string.navigation_item_favourite,
        icon = Icons.Outlined.Person
    )
}