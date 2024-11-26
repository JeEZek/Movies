package com.pomaskin.movies

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.pomaskin.movies.di.ApplicationComponent
import com.pomaskin.movies.di.DaggerApplicationComponent


class MovieApplication: Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as MovieApplication).component
}