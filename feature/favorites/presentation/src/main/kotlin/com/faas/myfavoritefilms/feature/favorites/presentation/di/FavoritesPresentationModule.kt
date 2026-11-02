package com.faas.myfavoritefilms.feature.favorites.presentation.di

import androidx.lifecycle.ViewModel
import com.faas.myfavoritefilms.core.di.ViewModelKey
import com.faas.myfavoritefilms.feature.favorites.presentation.FavoritesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FavoritesPresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    fun bindFavoritesViewModel(viewModel: FavoritesViewModel): ViewModel
}
