package com.faas.myfavoritefilms.core.network

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.common.DomainException
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> safeApiCall(
    crossinline onFailure: (cause: Throwable, error: DomainError) -> Unit = { _, _ -> },
    crossinline block: suspend () -> T,
): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        val error = e.toDomainError()
        onFailure(e, error)
        AppResult.Failure(error)
    }

fun Throwable.toDomainError(): DomainError = when (this) {
    is DomainException -> error
    is IOException -> DomainError.Network
    is HttpException -> when (code()) {
        401 -> DomainError.Unauthorized
        404 -> DomainError.NotFound
        429 -> DomainError.RateLimited
        in 500..599 -> DomainError.Server(code())
        else -> DomainError.Unknown
    }
    else -> DomainError.Unknown
}
