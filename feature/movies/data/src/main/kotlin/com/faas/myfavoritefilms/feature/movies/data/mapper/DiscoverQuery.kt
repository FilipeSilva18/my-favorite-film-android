package com.faas.myfavoritefilms.feature.movies.data.mapper

import com.faas.myfavoritefilms.feature.movies.data.remote.dto.GenreDto
import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieSort
import java.time.LocalDate

internal data class DiscoverQuery(
    val sortBy: String,
    val withGenres: String?,
    val releaseDateFrom: String?,
    val releaseDateTo: String?,
    val minRating: Double?,
    val minVoteCount: Int?,
)

internal fun MovieFilters.toDiscoverQuery(today: LocalDate): DiscoverQuery {
    val lastAllowedDate = yearTo?.let { LocalDate.of(it, 12, 31) }
    val releaseDateTo = when {
        sort == MovieSort.NEWEST -> listOfNotNull(lastAllowedDate, today).min()
        else -> lastAllowedDate
    }
    return DiscoverQuery(
        sortBy = when (sort) {
            MovieSort.POPULARITY -> "popularity.desc"
            MovieSort.RATING -> "vote_average.desc"
            MovieSort.NEWEST -> "primary_release_date.desc"
        },
        withGenres = genres.takeIf { it.isNotEmpty() }?.joinToString(",") { it.id.toString() },
        releaseDateFrom = yearFrom?.let { LocalDate.of(it, 1, 1).toString() },
        releaseDateTo = releaseDateTo?.toString(),
        minRating = minRating,
        minVoteCount = when {
            sort == MovieSort.RATING -> RATING_SORT_MIN_VOTES
            minRating != null -> MIN_RATING_MIN_VOTES
            else -> null
        },
    )
}

internal fun GenreDto.toDomain() = Genre(id = id, name = name)

private const val RATING_SORT_MIN_VOTES = 300
private const val MIN_RATING_MIN_VOTES = 100
