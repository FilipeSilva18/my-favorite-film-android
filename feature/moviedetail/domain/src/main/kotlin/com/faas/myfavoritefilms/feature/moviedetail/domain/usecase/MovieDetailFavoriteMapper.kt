package com.faas.myfavoritefilms.feature.moviedetail.domain.usecase

import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail

fun MovieDetail.toFavoriteMovie() = FavoriteMovie(
    id = id,
    title = title,
    posterUrl = posterUrl,
    rating = rating,
)
