package com.faas.myfavoritefilms.core.database.di

import android.content.Context
import androidx.room.Room
import com.faas.myfavoritefilms.core.database.MoviesDatabase
import com.faas.myfavoritefilms.core.database.dao.FavoriteMovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): MoviesDatabase =
        Room.databaseBuilder(context, MoviesDatabase::class.java, "movies.db").build()

    @Provides
    fun provideFavoriteMovieDao(db: MoviesDatabase): FavoriteMovieDao = db.favoriteMovieDao()
}
