package com.faas.myfavoritefilms.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.faas.myfavoritefilms.core.database.dao.FavoriteMovieDao
import com.faas.myfavoritefilms.core.database.entity.FavoriteMovieEntity

@Database(entities = [FavoriteMovieEntity::class], version = 1, exportSchema = true)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}
