package com.faas.myfavoritefilms.feature.movies.domain.model

data class MovieFilters(
    val genres: List<Genre> = emptyList(),
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val minRating: Double? = null,
    val sort: MovieSort = MovieSort.POPULARITY,
) {
    val hasCriteria: Boolean
        get() = genres.isNotEmpty() || yearFrom != null || yearTo != null || minRating != null || sort != MovieSort.POPULARITY
}

enum class MovieSort { POPULARITY, RATING, NEWEST }
