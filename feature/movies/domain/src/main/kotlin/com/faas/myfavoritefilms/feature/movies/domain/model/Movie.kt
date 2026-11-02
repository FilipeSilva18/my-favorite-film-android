package com.faas.myfavoritefilms.feature.movies.domain.model

import java.time.LocalDate

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: LocalDate?,
    val rating: Double,
)
