package com.pomaskin.movies.di


import com.pomaskin.movies.data.network.ApiFactory
import com.pomaskin.movies.data.network.ApiService
import com.pomaskin.movies.data.repository.MoviesRepositoryImpl
import com.pomaskin.movies.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: MoviesRepositoryImpl): MoviesRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}