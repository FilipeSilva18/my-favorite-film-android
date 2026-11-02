package com.faas.myfavoritefilms.feature.movies.data.repository

import androidx.paging.testing.asSnapshot
import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.testing.analytics.TestErrorReporter
import com.faas.myfavoritefilms.feature.movies.data.datasource.MoviesRemoteDataSource
import com.faas.myfavoritefilms.feature.movies.data.enqueueError
import com.faas.myfavoritefilms.feature.movies.data.enqueueResource
import com.faas.myfavoritefilms.feature.movies.data.remote.api.MoviesApi
import com.faas.myfavoritefilms.feature.movies.data.tmdbRetrofit
import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MoviesRepositoryImplTest {
    private val server = MockWebServer()
    private lateinit var repository: MoviesRepositoryImpl

    @Before
    fun setUp() {
        server.start()
        repository = MoviesRepositoryImpl(
            MoviesRemoteDataSource(server.tmdbRetrofit().create(MoviesApi::class.java)),
            TestErrorReporter(),
        )
    }

    @After
    fun tearDown() = server.close()

    @Test
    fun `popular pages until the last page and returns domain models`() = runTest {
        server.enqueueResource("tmdb/movie_popular_page1.json")
        server.enqueueResource("tmdb/movie_popular_page2.json")

        val movies = repository.popularMovies().asSnapshot {
            appendScrollWhile { true }
        }

        assertEquals(listOf(603, 1, 438631), movies.map { it.id })
        assertEquals("https://image.tmdb.org/t/p/w342/matrix.jpg", movies.first().posterUrl)
        assertEquals(2, server.requestCount)
    }

    @Test
    fun `search uses the search endpoint`() = runTest {
        server.enqueueResource("tmdb/search_movie_matrix.json")

        val movies = repository.searchMovies("matrix").asSnapshot()

        assertEquals(listOf("Matrix Reloaded"), movies.map { it.title })
        assertEquals("/3/search/movie", server.takeRequest().url.encodedPath)
    }

    @Test
    fun `discover sends the filters and returns domain models`() = runTest {
        server.enqueueResource("tmdb/discover_movie_heist.json")

        val movies = repository.discoverMovies(
            MovieFilters(genres = listOf(Genre(80, "Crime"), Genre(53, "Thriller")), yearFrom = 1990, yearTo = 1999),
        ).asSnapshot()

        val request = server.takeRequest()
        assertEquals("/3/discover/movie", request.url.encodedPath)
        assertEquals("80,53", request.url.queryParameter("with_genres"))
        assertEquals("1990-01-01", request.url.queryParameter("primary_release_date.gte"))
        assertEquals("1999-12-31", request.url.queryParameter("primary_release_date.lte"))
        assertEquals("popularity.desc", request.url.queryParameter("sort_by"))
        assertEquals(null, request.url.queryParameter("vote_average.gte"))
        assertEquals(listOf("Fogo contra Fogo"), movies.map { it.title })
    }

    @Test
    fun `genres are fetched once and cached`() = runTest {
        server.enqueueResource("tmdb/genre_movie_list.json")

        val first = repository.genres()
        val second = repository.genres()

        assertEquals(first, second)
        assertEquals(Genre(28, "Ação"), (first as AppResult.Success).data.first())
        assertEquals(1, server.requestCount)
        assertEquals("/3/genre/movie/list", server.takeRequest().url.encodedPath)
    }

    @Test
    fun `genres failure is not cached`() = runTest {
        server.enqueueError(500)
        server.enqueueResource("tmdb/genre_movie_list.json")

        assertEquals(AppResult.Failure(DomainError.Server(500)), repository.genres())
        assertEquals(4, (repository.genres() as AppResult.Success).data.size)
    }
}
