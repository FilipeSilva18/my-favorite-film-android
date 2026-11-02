package com.faas.myfavoritefilms.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.faas.myfavoritefilms.core.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movies ORDER BY savedAt DESC")
    fun observeAll(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT id FROM favorite_movies")
    fun observeIds(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :id)")
    fun observeExists(id: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :id)")
    suspend fun exists(id: Int): Boolean

    @Upsert
    suspend fun upsert(entity: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :id")
    suspend fun deleteById(id: Int)
}
