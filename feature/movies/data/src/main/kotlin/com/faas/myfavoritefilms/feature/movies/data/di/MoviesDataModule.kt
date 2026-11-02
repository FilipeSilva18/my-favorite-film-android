package com.faas.myfavoritefilms.feature.movies.data.di

import com.faas.myfavoritefilms.feature.movies.data.ai.AiMovieQueryInterpreter
import com.faas.myfavoritefilms.feature.movies.data.remote.api.MoviesApi
import com.faas.myfavoritefilms.feature.movies.data.repository.MoviesRepositoryImpl
import com.faas.myfavoritefilms.feature.movies.domain.interpreter.MovieQueryInterpreter
import com.faas.myfavoritefilms.feature.movies.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [MoviesDataBindsModule::class, MoviesApiModule::class])
object MoviesDataModule

@Module
internal interface MoviesDataBindsModule {
    @Binds
    fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    fun bindMovieQueryInterpreter(impl: AiMovieQueryInterpreter): MovieQueryInterpreter
}

@Module
internal object MoviesApiModule {
    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)
}
