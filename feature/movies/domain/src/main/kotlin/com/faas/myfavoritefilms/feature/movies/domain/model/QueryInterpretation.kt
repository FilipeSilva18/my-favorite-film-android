package com.faas.myfavoritefilms.feature.movies.domain.model

import com.faas.myfavoritefilms.core.common.DomainError

sealed interface QueryInterpretation {
    data class Understood(val filters: MovieFilters) : QueryInterpretation

    data object NotUnderstood : QueryInterpretation

    data class Unavailable(val error: DomainError) : QueryInterpretation
}
