package com.faas.myfavoritefilms.feature.moviedetail.presentation.di

import androidx.lifecycle.ViewModel
import com.faas.myfavoritefilms.core.di.ViewModelKey
import com.faas.myfavoritefilms.feature.moviedetail.presentation.MovieDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MovieDetailPresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    fun bindMovieDetailViewModel(viewModel: MovieDetailViewModel): ViewModel
}
