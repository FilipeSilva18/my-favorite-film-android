package com.faas.myfavoritefilms.feature.movies.data.mapper

import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieSort
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DiscoverQueryTest {
    private val today = LocalDate.of(2026, 11, 1)

    @Test
    fun `genres are combined with AND and years become release date bounds`() {
        val query = MovieFilters(
            genres = listOf(Genre(80, "Crime"), Genre(53, "Thriller")),
            yearFrom = 1990,
            yearTo = 1999,
        ).toDiscoverQuery(today)

        assertEquals(
            DiscoverQuery(
                sortBy = "popularity.desc",
                withGenres = "80,53",
                releaseDateFrom = "1990-01-01",
                releaseDateTo = "1999-12-31",
                minRating = null,
                minVoteCount = null,
            ),
            query,
        )
    }

    @Test
    fun `rating sort requires a minimum vote count`() {
        val query = MovieFilters(sort = MovieSort.RATING).toDiscoverQuery(today)

        assertEquals("vote_average.desc", query.sortBy)
        assertEquals(300, query.minVoteCount)
    }

    @Test
    fun `minimum rating requires a smaller minimum vote count`() {
        val query = MovieFilters(minRating = 7.5).toDiscoverQuery(today)

        assertEquals(7.5, query.minRating)
        assertEquals(100, query.minVoteCount)
    }

    @Test
    fun `newest sort never includes unreleased movies`() {
        assertEquals("2026-11-01", MovieFilters(sort = MovieSort.NEWEST).toDiscoverQuery(today).releaseDateTo)
        assertEquals(
            "2010-12-31",
            MovieFilters(yearTo = 2010, sort = MovieSort.NEWEST).toDiscoverQuery(today).releaseDateTo,
        )
    }
}
