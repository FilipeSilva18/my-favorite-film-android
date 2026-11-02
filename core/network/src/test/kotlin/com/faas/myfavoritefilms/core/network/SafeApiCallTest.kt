package com.faas.myfavoritefilms.core.network

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SafeApiCallTest {

    @Test
    fun `success returns Success`() = runTest {
        assertEquals(AppResult.Success(42), safeApiCall { 42 })
    }

    @Test
    fun `IOException becomes Network`() = runTest {
        assertEquals(AppResult.Failure(DomainError.Network), safeApiCall { throw IOException() })
    }

    @Test
    fun `HTTP codes map to the matching DomainError`() = runTest {
        assertEquals(DomainError.Unauthorized, http(401).toDomainError())
        assertEquals(DomainError.NotFound, http(404).toDomainError())
        assertEquals(DomainError.RateLimited, http(429).toDomainError())
        assertEquals(DomainError.Server(503), http(503).toDomainError())
        assertEquals(DomainError.Unknown, http(418).toDomainError())
    }

    private fun http(code: Int) = HttpException(Response.error<Unit>(code, "".toResponseBody()))
}
