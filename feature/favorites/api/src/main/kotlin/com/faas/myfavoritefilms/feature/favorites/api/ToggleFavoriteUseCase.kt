package com.faas.myfavoritefilms.feature.favorites.api

interface ToggleFavoriteUseCase {
    suspend operator fun invoke(movie: FavoriteMovie): Boolean
}
