package com.faas.myfavoritefilms.feature.favorites.data.datasource

import com.faas.myfavoritefilms.core.database.dao.FavoriteMovieDao
import com.faas.myfavoritefilms.core.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class FavoritesLocalDataSource @Inject constructor(
    private val dao: FavoriteMovieDao,
) {
    fun observeIds(): Flow<List<Int>> = dao.observeIds()

    fun observeExists(movieId: Int): Flow<Boolean> = dao.observeExists(movieId)

    fun observeAll(): Flow<List<FavoriteMovieEntity>> = dao.observeAll()

    suspend fun exists(movieId: Int): Boolean = dao.exists(movieId)

    suspend fun upsert(entity: FavoriteMovieEntity) = dao.upsert(entity)

    suspend fun delete(movieId: Int) = dao.deleteById(movieId)
}
