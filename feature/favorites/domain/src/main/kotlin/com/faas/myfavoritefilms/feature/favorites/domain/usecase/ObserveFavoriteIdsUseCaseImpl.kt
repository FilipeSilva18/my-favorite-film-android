package com.faas.myfavoritefilms.feature.favorites.domain.usecase

import com.faas.myfavoritefilms.feature.favorites.api.ObserveFavoriteIdsUseCase
import com.faas.myfavoritefilms.feature.favorites.domain.repository.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteIdsUseCaseImpl @Inject constructor(
    private val repository: FavoritesRepository,
) : ObserveFavoriteIdsUseCase {
    override operator fun invoke(): Flow<Set<Int>> = repository.observeFavoriteIds()
}
