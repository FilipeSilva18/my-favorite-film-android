package com.faas.myfavoritefilms.feature.moviedetail.data.mapper

import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.core.network.TmdbImages
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.MovieDetailDto
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail
import java.time.LocalDate

internal fun MovieDetailDto.toDomain(infoSections: List<SduiComponent>) = MovieDetail(
    id = id,
    title = title,
    tagline = tagline?.takeIf { it.isNotBlank() },
    overview = overview,
    posterUrl = TmdbImages.url(posterPath, TmdbImages.Size.W500),
    backdropUrl = TmdbImages.url(backdropPath, TmdbImages.Size.W780),
    releaseDate = releaseDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
    runtimeMinutes = runtime?.takeIf { it > 0 },
    rating = voteAverage,
    voteCount = voteCount,
    genres = genres.map { it.name },
    infoSections = infoSections,
)
