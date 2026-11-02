package com.faas.myfavoritefilms.core.network

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

class SafeApiCallRetrofitTest {
    @Serializable
    data class PingDto(val ok: Boolean, val extra: String = "default")

    interface PingApi {
        @GET("ping")
        suspend fun ping(): PingDto
    }

    private val server = MockWebServer()
    private lateinit var api: PingApi

    @Before
    fun setUp() {
        server.start()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(TmdbJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PingApi::class.java)
    }

    @After
    fun tearDown() = server.close()

    private fun respond(code: Int, body: String = "{}") =
        server.enqueue(MockResponse.Builder().code(code).body(body).build())

    @Test
    fun `200 with unknown and null fields becomes Success`() = runTest {
        respond(200, """{ "ok": true, "extra": null, "campo_novo": 1 }""")

        assertEquals(AppResult.Success(PingDto(ok = true)), safeApiCall { api.ping() })
    }

    @Test
    fun `HTTP status codes become DomainError`() = runTest {
        val cases = mapOf(
            401 to DomainError.Unauthorized,
            404 to DomainError.NotFound,
            429 to DomainError.RateLimited,
            503 to DomainError.Server(503),
        )
        cases.forEach { (code, expected) ->
            respond(code)
            assertEquals("HTTP $code", AppResult.Failure(expected), safeApiCall { api.ping() })
        }
    }

    @Test
    fun `JSON outside the contract becomes Unknown, not a crash`() = runTest {
        respond(200, """{ "sem_ok": true }""")

        assertEquals(AppResult.Failure(DomainError.Unknown), safeApiCall { api.ping() })
    }
}
