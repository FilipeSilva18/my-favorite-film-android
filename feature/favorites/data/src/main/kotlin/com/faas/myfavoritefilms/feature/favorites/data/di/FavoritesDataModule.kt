package com.faas.myfavoritefilms.feature.favorites.data.di

import com.faas.myfavoritefilms.feature.favorites.api.ObserveFavoriteIdsUseCase
import com.faas.myfavoritefilms.feature.favorites.api.ObserveIsFavoriteUseCase
import com.faas.myfavoritefilms.feature.favorites.api.ToggleFavoriteUseCase
import com.faas.myfavoritefilms.feature.favorites.data.repository.FavoritesRepositoryImpl
import com.faas.myfavoritefilms.feature.favorites.domain.repository.FavoritesRepository
import com.faas.myfavoritefilms.feature.favorites.domain.usecase.ObserveFavoriteIdsUseCaseImpl
import com.faas.myfavoritefilms.feature.favorites.domain.usecase.ObserveIsFavoriteUseCaseImpl
import com.faas.myfavoritefilms.feature.favorites.domain.usecase.ToggleFavoriteUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [FavoritesDataBindsModule::class])
object FavoritesDataModule

@Module
internal interface FavoritesDataBindsModule {
    @Binds
    @Singleton
    fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    fun bindObserveFavoriteIds(impl: ObserveFavoriteIdsUseCaseImpl): ObserveFavoriteIdsUseCase

    @Binds
    fun bindObserveIsFavorite(impl: ObserveIsFavoriteUseCaseImpl): ObserveIsFavoriteUseCase

    @Binds
    fun bindToggleFavorite(impl: ToggleFavoriteUseCaseImpl): ToggleFavoriteUseCase
}
