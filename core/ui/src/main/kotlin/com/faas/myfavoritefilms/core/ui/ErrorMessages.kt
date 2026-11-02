package com.faas.myfavoritefilms.core.ui

import androidx.annotation.StringRes
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.common.DomainException

@StringRes
fun DomainError.messageRes(): Int = when (this) {
    DomainError.Network -> R.string.error_network
    DomainError.Unauthorized -> R.string.error_unauthorized
    DomainError.NotFound -> R.string.error_not_found
    DomainError.RateLimited -> R.string.error_rate_limited
    is DomainError.Server -> R.string.error_server
    DomainError.Unknown -> R.string.error_unknown
}

fun Throwable.asDomainError(): DomainError = (this as? DomainException)?.error ?: DomainError.Unknown
