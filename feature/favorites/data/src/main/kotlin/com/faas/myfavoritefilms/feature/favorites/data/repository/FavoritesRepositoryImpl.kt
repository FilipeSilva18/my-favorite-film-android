package com.faas.myfavoritefilms.feature.favorites.data.repository

import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.data.datasource.FavoritesLocalDataSource
import com.faas.myfavoritefilms.feature.favorites.data.mapper.toDomain
import com.faas.myfavoritefilms.feature.favorites.data.mapper.toEntity
import com.faas.myfavoritefilms.feature.favorites.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class FavoritesRepositoryImpl @Inject constructor(
    private val localDataSource: FavoritesLocalDataSource,
) : FavoritesRepository {

    override fun observeFavoriteIds(): Flow<Set<Int>> = localDataSource.observeIds().map { it.toSet() }

    override fun observeIsFavorite(movieId: Int): Flow<Boolean> = localDataSource.observeExists(movieId)

    override fun observeFavorites(): Flow<List<FavoriteMovie>> =
        localDataSource.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun isFavorite(movieId: Int): Boolean = localDataSource.exists(movieId)

    override suspend fun add(movie: FavoriteMovie) =
        localDataSource.upsert(movie.toEntity(savedAt = System.currentTimeMillis()))

    override suspend fun remove(movieId: Int) = localDataSource.delete(movieId)
}
