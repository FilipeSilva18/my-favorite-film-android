package com.faas.myfavoritefilms.feature.favorites.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeFavorites {
    private val favorites = MutableStateFlow<Map<Int, FavoriteMovie>>(emptyMap())

    val current: List<FavoriteMovie> get() = favorites.value.values.toList()

    fun isFavorite(movieId: Int): Boolean = movieId in favorites.value

    fun add(movie: FavoriteMovie) = favorites.update { it + (movie.id to movie) }

    val observeFavoriteIds: ObserveFavoriteIdsUseCase = object : ObserveFavoriteIdsUseCase {
        override fun invoke(): Flow<Set<Int>> = favorites.map { it.keys }
    }

    val observeIsFavorite: ObserveIsFavoriteUseCase = object : ObserveIsFavoriteUseCase {
        override fun invoke(movieId: Int): Flow<Boolean> = favorites.map { movieId in it }
    }

    val toggleFavorite: ToggleFavoriteUseCase = object : ToggleFavoriteUseCase {
        override suspend fun invoke(movie: FavoriteMovie): Boolean {
            val added = movie.id !in favorites.value
            favorites.update { if (added) it + (movie.id to movie) else it - movie.id }
            return added
        }
    }
}
