package com.faas.myfavoritefilms.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class AppResultTest {

    @Test
    fun `map transforms only success`() {
        assertEquals(AppResult.Success(4), AppResult.Success(2).map { it * 2 })

        val failure: AppResult<Int> = AppResult.Failure(DomainError.Network)
        assertEquals(failure, failure.map { it * 2 })
    }

    @Test
    fun `fold picks the right branch`() {
        val ok = AppResult.Success("x").fold(onSuccess = { "ok:$it" }, onFailure = { "erro" })
        val ko = AppResult.Failure(DomainError.NotFound).fold(onSuccess = { "ok" }, onFailure = { "erro:$it" })

        assertEquals("ok:x", ok)
        assertEquals("erro:NotFound", ko)
    }

    @Test
    fun `DomainException carries the DomainError`() {
        assertEquals(DomainError.RateLimited, DomainException(DomainError.RateLimited).error)
    }
}
