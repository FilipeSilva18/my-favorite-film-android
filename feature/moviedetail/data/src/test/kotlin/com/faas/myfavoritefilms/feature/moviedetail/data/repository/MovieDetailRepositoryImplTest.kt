package com.faas.myfavoritefilms.feature.moviedetail.data.repository

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.core.network.TmdbJson
import com.faas.myfavoritefilms.core.network.sdui.SduiParser
import com.faas.myfavoritefilms.core.testing.analytics.TestErrorReporter
import com.faas.myfavoritefilms.feature.moviedetail.data.datasource.MovieDetailRemoteDataSource
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.api.MovieDetailApi
import com.faas.myfavoritefilms.feature.moviedetail.data.sdui.MovieDetailLayoutService
import com.faas.myfavoritefilms.feature.moviedetail.data.sdui.testLayoutStrings
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.time.LocalDate

class MovieDetailRepositoryImplTest {
    private val server = MockWebServer()
    private val errorReporter = TestErrorReporter()
    private lateinit var repository: MovieDetailRepositoryImpl

    @Before
    fun setUp() {
        server.start()
        val api = Retrofit.Builder()
            .baseUrl(server.url("/3/"))
            .addConverterFactory(TmdbJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MovieDetailApi::class.java)
        repository = MovieDetailRepositoryImpl(
            MovieDetailRemoteDataSource(api),
            MovieDetailLayoutService(testLayoutStrings),
            SduiParser(TmdbJson),
            errorReporter,
        )
    }

    @After
    fun tearDown() {
        runCatching { server.close() }
    }

    private fun enqueueFixture() {
        val body = checkNotNull(javaClass.classLoader?.getResource("tmdb/movie_detail_603.json")).readText()
        server.enqueue(MockResponse.Builder().body(body).build())
    }

    @Test
    fun `full detail with TMDB data and server driven sections`() = runTest {
        enqueueFixture()

        val result = repository.getMovieDetail(603)

        val request = server.takeRequest()
        assertEquals("/3/movie/603", request.url.encodedPath)
        assertEquals(MovieDetailApi.APPEND_CREDITS_AND_VIDEOS, request.url.queryParameter("append_to_response"))
        val movie = (result as AppResult.Success<MovieDetail>).data
        assertEquals("Matrix", movie.title)
        assertEquals("Bem-vindo ao mundo real.", movie.tagline)
        assertEquals(LocalDate.of(1999, 3, 30), movie.releaseDate)
        assertEquals(136, movie.runtimeMinutes)
        assertEquals(listOf("Ação", "Ficção científica"), movie.genres)
        assertEquals("https://image.tmdb.org/t/p/w780/matrix_backdrop.jpg", movie.backdropUrl)

        val ficha = movie.infoSections.filterIsInstance<SduiComponent.KeyValueList>().single()
        val rows = ficha.items.associate { it.label to it.value }
        assertEquals("$63,000,000", rows["Budget"])
        assertEquals("$463,517,383", rows["Box office"])

        val produtoras = movie.infoSections.filterIsInstance<SduiComponent.ChipGroup>().first { it.title == "Production companies" }
        assertEquals(listOf("Village Roadshow Pictures", "Warner Bros. Pictures"), produtoras.chips)

        assertTrue(movie.infoSections.any { it is SduiComponent.LinkButton && it.url.endsWith("tt0133093") })

        val cast = movie.infoSections.filterIsInstance<SduiComponent.PersonCarousel>().single()
        assertEquals(listOf("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"), cast.people.map { it.name })
        assertEquals("https://image.tmdb.org/t/p/w185/keanu.jpg", cast.people.first().imageUrl)

        val videos = movie.infoSections.filterIsInstance<SduiComponent.VideoCarousel>().single()
        assertEquals(
            listOf("https://www.youtube.com/watch?v=trailer1", "https://www.youtube.com/watch?v=teaser1"),
            videos.videos.map { it.url },
        )
    }

    @Test
    fun `404 becomes NotFound and connection failure becomes Network`() = runTest {
        server.enqueue(MockResponse.Builder().code(404).body("{}").build())
        assertEquals(AppResult.Failure(DomainError.NotFound), repository.getMovieDetail(999))

        server.close()
        assertEquals(AppResult.Failure(DomainError.Network), repository.getMovieDetail(603))

        assertTrue("404 and no connection are expected, not non fatal", errorReporter.errors.isEmpty())
    }

    @Test
    fun `500 error is reported as non fatal`() = runTest {
        server.enqueue(MockResponse.Builder().code(500).body("{}").build())

        assertEquals(AppResult.Failure(DomainError.Server(500)), repository.getMovieDetail(603))
        assertEquals(listOf("movie_detail"), errorReporter.errors.map { it.second["origin"] })
    }
}
