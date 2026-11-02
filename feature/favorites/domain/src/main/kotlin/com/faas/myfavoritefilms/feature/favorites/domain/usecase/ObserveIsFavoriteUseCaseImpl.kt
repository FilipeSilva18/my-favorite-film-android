package com.faas.myfavoritefilms.feature.favorites.domain.usecase

import com.faas.myfavoritefilms.feature.favorites.api.ObserveIsFavoriteUseCase
import com.faas.myfavoritefilms.feature.favorites.domain.repository.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveIsFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoritesRepository,
) : ObserveIsFavoriteUseCase {
    override operator fun invoke(movieId: Int): Flow<Boolean> = repository.observeIsFavorite(movieId)
}
