package com.faas.myfavoritefilms.feature.movies.data.mapper

import com.faas.myfavoritefilms.feature.movies.data.remote.dto.MovieDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class MovieMapperTest {

    @Test
    fun `builds poster url and parses the date`() {
        val movie = MovieDto(
            id = 603,
            title = "Matrix",
            posterPath = "/matrix.jpg",
            releaseDate = "1999-03-30",
            voteAverage = 8.2,
        ).toDomain()

        assertEquals("https://image.tmdb.org/t/p/w342/matrix.jpg", movie.posterUrl)
        assertEquals(LocalDate.of(1999, 3, 30), movie.releaseDate)
    }

    @Test
    fun `blank date and missing poster become null`() {
        val movie = MovieDto(id = 1, title = "Sem data", releaseDate = "", posterPath = null).toDomain()

        assertNull(movie.releaseDate)
        assertNull(movie.posterUrl)
    }
}
