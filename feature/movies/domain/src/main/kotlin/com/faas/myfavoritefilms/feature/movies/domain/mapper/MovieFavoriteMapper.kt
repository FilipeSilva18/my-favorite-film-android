package com.faas.myfavoritefilms.feature.movies.domain.mapper

import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie

fun Movie.toFavoriteMovie() = FavoriteMovie(
    id = id,
    title = title,
    posterUrl = posterUrl,
    rating = rating,
)
