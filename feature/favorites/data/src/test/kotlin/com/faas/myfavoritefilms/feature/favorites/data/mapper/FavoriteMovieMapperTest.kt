package com.faas.myfavoritefilms.feature.favorites.data.mapper

import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteMovieMapperTest {

    @Test
    fun `domain to entity round trip keeps the data`() {
        val movie = FavoriteMovie(id = 603, title = "Matrix", posterUrl = "url", rating = 8.2)
        assertEquals(movie, movie.toEntity(savedAt = 1L).toDomain())
    }
}
