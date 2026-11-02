package com.faas.myfavoritefilms.core.ai

sealed class AiException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class Unavailable(message: String, cause: Throwable? = null) : AiException(message, cause)

    class QuotaExceeded(cause: Throwable? = null) : AiException("AI quota exceeded", cause)

    class Failed(message: String, cause: Throwable? = null) : AiException(message, cause)
}
