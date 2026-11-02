package com.faas.myfavoritefilms.feature.favorites.domain

import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeFavoritesRepository : FavoritesRepository {
    private val favorites = MutableStateFlow<Map<Int, FavoriteMovie>>(emptyMap())

    override fun observeFavoriteIds(): Flow<Set<Int>> = favorites.map { it.keys }
    override fun observeIsFavorite(movieId: Int): Flow<Boolean> = favorites.map { movieId in it }
    override fun observeFavorites(): Flow<List<FavoriteMovie>> = favorites.map { it.values.toList() }

    override suspend fun isFavorite(movieId: Int): Boolean = movieId in favorites.value
    override suspend fun add(movie: FavoriteMovie) = favorites.update { it + (movie.id to movie) }
    override suspend fun remove(movieId: Int) = favorites.update { it - movieId }
}
