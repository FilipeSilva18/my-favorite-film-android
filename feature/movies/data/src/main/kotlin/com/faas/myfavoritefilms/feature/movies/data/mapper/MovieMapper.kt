package com.faas.myfavoritefilms.feature.movies.data.mapper

import com.faas.myfavoritefilms.core.network.TmdbImages
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.MovieDto
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie
import java.time.LocalDate

internal fun MovieDto.toDomain() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterUrl = TmdbImages.url(posterPath, TmdbImages.Size.W342),

    releaseDate = releaseDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
    rating = voteAverage,
)
