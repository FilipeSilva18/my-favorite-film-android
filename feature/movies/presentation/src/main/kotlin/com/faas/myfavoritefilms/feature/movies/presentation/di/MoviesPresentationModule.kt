package com.faas.myfavoritefilms.feature.movies.presentation.di

import androidx.lifecycle.ViewModel
import com.faas.myfavoritefilms.core.di.ViewModelKey
import com.faas.myfavoritefilms.feature.movies.presentation.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MoviesPresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    fun bindMoviesViewModel(viewModel: MoviesViewModel): ViewModel
}
