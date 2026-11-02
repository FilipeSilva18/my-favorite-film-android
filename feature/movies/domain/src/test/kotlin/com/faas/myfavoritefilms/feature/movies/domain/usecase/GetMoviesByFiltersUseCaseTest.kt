package com.faas.myfavoritefilms.feature.movies.domain.usecase

import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieSort
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMoviesByFiltersUseCaseTest {
    private val repository = FakeMoviesRepository()

    @Test
    fun `discovers movies with the given filters`() {
        val filters = MovieFilters(genres = listOf(Genre(28, "Action")), yearFrom = 1990, sort = MovieSort.RATING)

        GetMoviesByFiltersUseCase(repository)(filters)

        assertEquals(FakeMoviesRepository.Call.Discover(filters), repository.lastCall)
    }
}
