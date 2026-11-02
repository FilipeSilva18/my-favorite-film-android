package com.faas.myfavoritefilms.feature.favorites.presentation.model

import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.core.ui.movie.formatRating
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie

internal fun FavoriteMovie.toCardUi() = MovieCardUi(
    id = id,
    title = title,
    posterUrl = posterUrl,
    year = null,
    rating = rating,
    ratingLabel = formatRating(rating),
    isFavorite = true,
)

internal fun MovieCardUi.toFavoriteMovie() =
    FavoriteMovie(id = id, title = title, posterUrl = posterUrl, rating = rating)
