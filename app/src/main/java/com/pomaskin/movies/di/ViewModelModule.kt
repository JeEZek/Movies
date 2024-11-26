package com.pomaskin.movies.di

import androidx.lifecycle.ViewModel
import com.pomaskin.movies.presentation.main.MainViewModel
import com.pomaskin.movies.presentation.popular.PopularViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PopularViewModel::class)
    fun bindPopularViewModel(viewModel: PopularViewModel): ViewModel
}