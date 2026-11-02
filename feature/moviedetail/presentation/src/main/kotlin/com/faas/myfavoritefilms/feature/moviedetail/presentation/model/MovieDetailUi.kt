package com.faas.myfavoritefilms.feature.moviedetail.presentation.model

import androidx.compose.runtime.Immutable
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.core.ui.movie.formatRating
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Immutable
data class MovieDetailUi(
    val id: Int,
    val title: String,
    val tagline: String?,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDateLabel: String?,
    val year: String?,
    val runtimeMinutes: Int?,
    val ratingLabel: String,
    val voteCountLabel: String,
    val genres: List<String>,
    val infoSections: List<SduiComponent>,
)

internal fun MovieDetail.toUi(locale: Locale = Locale.getDefault()) = MovieDetailUi(
    id = id,
    title = title,
    tagline = tagline,
    overview = overview,
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
    releaseDateLabel = releaseDate?.format(
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale),
    ),
    year = releaseDate?.year?.toString(),
    runtimeMinutes = runtimeMinutes,
    ratingLabel = formatRating(rating),
    voteCountLabel = NumberFormat.getIntegerInstance(locale).format(voteCount),
    genres = genres,
    infoSections = infoSections,
)
