package com.faas.myfavoritefilms.core.analytics

import com.faas.myfavoritefilms.core.common.DomainError

interface ErrorReporter {
    fun recordNonFatal(throwable: Throwable, attributes: Map<String, String> = emptyMap())
}

interface ErrorTracker {
    fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>)
}

object NoOpErrorReporter : ErrorReporter {
    override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) = Unit
}

fun ErrorReporter.reportIfUnexpected(throwable: Throwable, error: DomainError, origin: String) {
    if (error is DomainError.Server || error == DomainError.Unknown) {
        recordNonFatal(
            throwable = throwable,
            attributes = mapOf(
                ErrorAttributes.ORIGIN to origin,
                ErrorAttributes.DOMAIN_ERROR to error.toString(),
            ),
        )
    }
}

object ErrorAttributes {
    const val ORIGIN = "origin"
    const val DOMAIN_ERROR = "domain_error"
}
