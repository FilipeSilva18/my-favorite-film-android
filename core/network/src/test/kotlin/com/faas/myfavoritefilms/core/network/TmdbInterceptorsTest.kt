package com.faas.myfavoritefilms.core.network

import com.faas.myfavoritefilms.core.network.interceptor.TmdbAuthInterceptor
import com.faas.myfavoritefilms.core.network.interceptor.TmdbLanguageInterceptor
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Locale

class TmdbInterceptorsTest {
    private val server = MockWebServer()
    private lateinit var defaultLocale: Locale

    private val client = OkHttpClient.Builder()
        .addInterceptor(TmdbAuthInterceptor(accessToken = TEST_TOKEN))
        .addInterceptor(TmdbLanguageInterceptor())
        .build()

    @Before
    fun setUp() {
        defaultLocale = Locale.getDefault()
        server.start()
    }

    @After
    fun tearDown() {
        Locale.setDefault(defaultLocale)
        server.close()
    }

    private fun get(path: String) {
        server.enqueue(MockResponse.Builder().body("{}").build())
        client.newCall(Request.Builder().url(server.url(path)).build()).execute().close()
    }

    @Test
    fun `every request sends the token as Bearer header and never in the URL`() {
        get("/movie/popular?page=1")

        val request = server.takeRequest()
        assertEquals("Bearer $TEST_TOKEN", request.headers["Authorization"])
        assertEquals("application/json", request.headers["Accept"])
        assertTrue("token must not be in the URL", "api_key" !in request.url.toString())
        assertTrue("token must not be in the URL", TEST_TOKEN !in request.url.toString())
    }

    @Test
    fun `adds the device language keeping existing params`() {
        Locale.setDefault(Locale.forLanguageTag("pt-BR"))

        get("/search/movie?query=matrix&page=2")

        val url = server.takeRequest().url
        assertEquals("pt-BR", url.queryParameter("language"))
        assertEquals("matrix", url.queryParameter("query"))
        assertEquals("2", url.queryParameter("page"))
    }

    private companion object {
        const val TEST_TOKEN = "test-read-access-token"
    }
}
