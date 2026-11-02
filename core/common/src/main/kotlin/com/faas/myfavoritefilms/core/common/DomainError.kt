package com.faas.myfavoritefilms.core.common

sealed interface DomainError {
    data object Network : DomainError
    data object Unauthorized : DomainError
    data object NotFound : DomainError
    data object RateLimited : DomainError
    data class Server(val code: Int) : DomainError
    data object Unknown : DomainError
}

class DomainException(val error: DomainError) : Exception(error.toString())
