package com.faas.myfavoritefilms.feature.favorites.domain.repository

import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavoriteIds(): Flow<Set<Int>>
    fun observeIsFavorite(movieId: Int): Flow<Boolean>
    fun observeFavorites(): Flow<List<FavoriteMovie>>
    suspend fun isFavorite(movieId: Int): Boolean
    suspend fun add(movie: FavoriteMovie)
    suspend fun remove(movieId: Int)
}
