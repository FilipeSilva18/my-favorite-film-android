package com.faas.myfavoritefilms.feature.favorites.api

import kotlinx.coroutines.flow.Flow

interface ObserveIsFavoriteUseCase {
    operator fun invoke(movieId: Int): Flow<Boolean>
}
