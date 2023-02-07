package com.test.gamesapp.core.di

import com.test.gamesapp.core.domain.repository.MovieRepository
import com.test.gamesapp.core.data.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class MovieModule {

    @Binds
    @Singleton
    abstract fun provideMovieRepository(movieRepo: MovieRepositoryImpl): MovieRepository

}