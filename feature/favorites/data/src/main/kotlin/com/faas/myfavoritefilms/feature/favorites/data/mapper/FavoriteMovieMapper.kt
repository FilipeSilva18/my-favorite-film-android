package com.faas.myfavoritefilms.feature.favorites.data.mapper

import com.faas.myfavoritefilms.core.database.entity.FavoriteMovieEntity
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie

internal fun FavoriteMovieEntity.toDomain() = FavoriteMovie(
    id = id,
    title = title,
    posterUrl = posterUrl,
    rating = rating,
)

internal fun FavoriteMovie.toEntity(savedAt: Long) = FavoriteMovieEntity(
    id = id,
    title = title,
    posterUrl = posterUrl,
    rating = rating,
    savedAt = savedAt,
)
