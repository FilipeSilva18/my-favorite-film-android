package com.faas.myfavoritefilms.feature.movies.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class AiMovieFiltersDto(
    val genreIds: List<Int> = emptyList(),
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val minRating: Double? = null,
    val sort: String? = null,
)
