package com.faas.myfavoritefilms.feature.moviedetail.data.mapper

import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.MovieDetailDto
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.NamedDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieDetailMapperTest {

    @Test
    fun `builds detail image urls and clears blank fields`() {
        val detail = MovieDetailDto(
            id = 603,
            title = "Matrix",
            tagline = "",
            posterPath = "/p.jpg",
            backdropPath = "/b.jpg",
            runtime = 0,
            genres = listOf(NamedDto("Ação")),
        ).toDomain(infoSections = emptyList())

        assertEquals("https://image.tmdb.org/t/p/w500/p.jpg", detail.posterUrl)
        assertEquals("https://image.tmdb.org/t/p/w780/b.jpg", detail.backdropUrl)
        assertNull(detail.tagline)
        assertNull(detail.runtimeMinutes)
        assertEquals(listOf("Ação"), detail.genres)
    }
}
