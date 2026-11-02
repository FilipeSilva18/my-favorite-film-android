package com.faas.myfavoritefilms.feature.moviedetail.data.di

import android.content.Context
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.api.MovieDetailApi
import com.faas.myfavoritefilms.feature.moviedetail.data.repository.MovieDetailRepositoryImpl
import com.faas.myfavoritefilms.feature.moviedetail.data.sdui.MovieDetailLayoutStrings
import com.faas.myfavoritefilms.feature.moviedetail.domain.repository.MovieDetailRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [MovieDetailBindsModule::class, MovieDetailApiModule::class])
object MovieDetailDataModule

@Module
internal interface MovieDetailBindsModule {
    @Binds
    fun bindMovieDetailRepository(impl: MovieDetailRepositoryImpl): MovieDetailRepository
}

@Module
internal object MovieDetailApiModule {
    @Provides
    @Singleton
    fun provideMovieDetailApi(retrofit: Retrofit): MovieDetailApi = retrofit.create(MovieDetailApi::class.java)

    @Provides
    fun provideMovieDetailLayoutStrings(context: Context): MovieDetailLayoutStrings =
        MovieDetailLayoutStrings.from(context.resources)
}
