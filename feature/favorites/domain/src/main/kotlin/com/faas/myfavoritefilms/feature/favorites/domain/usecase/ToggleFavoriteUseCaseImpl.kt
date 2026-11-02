package com.faas.myfavoritefilms.feature.favorites.domain.usecase

import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.api.ToggleFavoriteUseCase
import com.faas.myfavoritefilms.feature.favorites.domain.repository.FavoritesRepository
import javax.inject.Inject

class ToggleFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoritesRepository,
) : ToggleFavoriteUseCase {
    override suspend operator fun invoke(movie: FavoriteMovie): Boolean =
        if (repository.isFavorite(movie.id)) {
            repository.remove(movie.id)
            false
        } else {
            repository.add(movie)
            true
        }
}
