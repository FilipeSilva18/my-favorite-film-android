package com.faas.myfavoritefilms.feature.movies.data.remote

import com.faas.myfavoritefilms.feature.movies.data.enqueueResource
import com.faas.myfavoritefilms.feature.movies.data.remote.api.MoviesApi
import com.faas.myfavoritefilms.feature.movies.data.tmdbRetrofit
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MoviesApiContractTest {
    private val server = MockWebServer()
    private lateinit var api: MoviesApi

    @Before
    fun setUp() {
        server.start()
        api = server.tmdbRetrofit().create(MoviesApi::class.java)
    }

    @After
    fun tearDown() = server.close()

    @Test
    fun `popular calls movie popular with the page and parses the paged envelope`() = runTest {
        server.enqueueResource("tmdb/movie_popular_page1.json")

        val response = api.getPopular(page = 1)

        val request = server.takeRequest()
        assertEquals("/3/movie/popular", request.url.encodedPath)
        assertEquals("1", request.url.queryParameter("page"))

        assertEquals(1, response.page)
        assertEquals(2, response.totalPages)
        val matrix = response.results.first()
        assertEquals(603, matrix.id)
        assertEquals("/matrix.jpg", matrix.posterPath)
        assertEquals("1999-03-30", matrix.releaseDate)
        assertEquals(8.2, matrix.voteAverage, 0.0)
    }

    @Test
    fun `null or blank fields do not break parsing`() = runTest {
        server.enqueueResource("tmdb/movie_popular_page1.json")

        val semPoster = api.getPopular(page = 1).results[1]

        assertNull(semPoster.posterPath)
        assertEquals("", semPoster.releaseDate)
        assertEquals(0.0, semPoster.voteAverage, 0.0)
    }

    @Test
    fun `search sends query, page and include_adult false`() = runTest {
        server.enqueueResource("tmdb/search_movie_matrix.json")

        val response = api.search(query = "matrix", page = 1)

        val url = server.takeRequest().url
        assertEquals("/3/search/movie", url.encodedPath)
        assertEquals("matrix", url.queryParameter("query"))
        assertEquals("1", url.queryParameter("page"))
        assertEquals("false", url.queryParameter("include_adult"))
        assertEquals(listOf("Matrix Reloaded"), response.results.map { it.title })
    }
}
