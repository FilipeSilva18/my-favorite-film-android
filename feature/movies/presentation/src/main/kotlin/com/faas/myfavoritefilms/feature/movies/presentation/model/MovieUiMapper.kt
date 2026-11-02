package com.faas.myfavoritefilms.feature.movies.presentation.model

import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.core.ui.movie.formatRating
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie

internal fun Movie.toCardUi(isFavorite: Boolean) = MovieCardUi(
    id = id,
    title = title,
    posterUrl = posterUrl,
    year = releaseDate?.year?.toString(),
    rating = rating,
    ratingLabel = formatRating(rating),
    isFavorite = isFavorite,
)

internal fun MovieCardUi.toFavoriteMovie() =
    FavoriteMovie(id = id, title = title, posterUrl = posterUrl, rating = rating)
