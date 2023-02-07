package com.test.gamesapp.feature_movie.di

import com.test.gamesapp.feature_movie.presentation.component.adapter.AdapterGenre
import com.test.gamesapp.feature_movie.presentation.component.adapter.AdapterMovie
import com.test.gamesapp.feature_movie.presentation.component.adapter.AdapterTVShow
import com.test.gamesapp.feature_movie.presentation.component.adapter.AdapterReview
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UiMovieModule {

    @Provides
    @Singleton
    fun provideAdapterTvShow(): AdapterTVShow {
        return AdapterTVShow()
    }

    @Provides
    @Singleton
    fun provideAdapterMovie(): AdapterMovie {
        return AdapterMovie()
    }


    @Provides
    @Singleton
    fun provideAdapterGenre(): AdapterGenre {
        return AdapterGenre()
    }

    @Provides
    @Singleton
    fun provideReviewAdapter(): AdapterReview {
        return AdapterReview()
    }


}