package com.faas.myfavoritefilms.core.network

object TmdbImages {
    private const val BASE = "https://image.tmdb.org/t/p/"

    enum class Size(val path: String) {
        W185("w185"),
        W342("w342"),
        W500("w500"),
        W780("w780"),
        ORIGINAL("original"),
    }

    fun url(path: String?, size: Size): String? =
        path?.takeIf { it.isNotBlank() }?.let { "$BASE${size.path}$it" }
}
