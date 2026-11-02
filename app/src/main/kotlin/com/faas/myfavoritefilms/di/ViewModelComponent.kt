package com.faas.myfavoritefilms.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import com.faas.myfavoritefilms.core.di.DaggerViewModelFactory
import com.faas.myfavoritefilms.core.di.ViewModelGraph
import com.faas.myfavoritefilms.core.di.ViewModelGraphFactory
import com.faas.myfavoritefilms.core.di.ViewModelMultibindsModule
import com.faas.myfavoritefilms.feature.favorites.presentation.di.FavoritesPresentationModule
import com.faas.myfavoritefilms.feature.settings.presentation.di.SettingsPresentationModule
import com.faas.myfavoritefilms.presentation.AppViewModelModule
import com.faas.myfavoritefilms.feature.moviedetail.presentation.di.MovieDetailPresentationModule
import com.faas.myfavoritefilms.feature.movies.presentation.di.MoviesPresentationModule
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ViewModelMultibindsModule::class,

        MoviesPresentationModule::class,
        MovieDetailPresentationModule::class,
        FavoritesPresentationModule::class,
        SettingsPresentationModule::class,
        AppViewModelModule::class,
    ],
)
interface ViewModelComponent : ViewModelGraph {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance savedStateHandle: SavedStateHandle): ViewModelComponent
    }
}

@Module(subcomponents = [ViewModelComponent::class])
abstract class ViewModelBindingModule {
    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    companion object {
        @Provides
        fun provideViewModelGraphFactory(factory: ViewModelComponent.Factory): ViewModelGraphFactory =
            ViewModelGraphFactory { handle -> factory.create(handle) }
    }
}
