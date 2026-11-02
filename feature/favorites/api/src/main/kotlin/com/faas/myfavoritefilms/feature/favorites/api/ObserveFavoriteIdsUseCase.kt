package com.faas.myfavoritefilms.feature.favorites.api

import kotlinx.coroutines.flow.Flow

interface ObserveFavoriteIdsUseCase {
    operator fun invoke(): Flow<Set<Int>>
}
