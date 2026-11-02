package com.faas.myfavoritefilms.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String?,
    val rating: Double,
    val savedAt: Long,
)
