package com.faas.myfavoritefilms.core.ui.movie

import androidx.compose.runtime.Immutable
import java.util.Locale

@Immutable
data class MovieCardUi(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val year: String?,
    val rating: Double,
    val ratingLabel: String,
    val isFavorite: Boolean,
)

fun formatRating(rating: Double): String = String.format(Locale.US, "%.1f", rating)
