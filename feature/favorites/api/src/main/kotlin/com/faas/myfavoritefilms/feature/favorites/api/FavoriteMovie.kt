package com.faas.myfavoritefilms.feature.favorites.api

data class FavoriteMovie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val rating: Double,
)
