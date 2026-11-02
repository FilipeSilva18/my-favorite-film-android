package com.faas.myfavoritefilms.feature.movies.presentation.model

import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieSort

enum class SearchMode { TITLE, AI }

sealed interface AiSearchUiState {
    data object Idle : AiSearchUiState

    data object Interpreting : AiSearchUiState

    data class Applied(val chips: List<FilterChipUi>) : AiSearchUiState

    data class Fallback(val reason: FallbackReason) : AiSearchUiState
}

enum class FallbackReason { NOT_UNDERSTOOD, UNAVAILABLE }

sealed interface FilterChipUi {
    data class Genre(val genreId: Int, val name: String) : FilterChipUi

    data class Years(val from: Int?, val to: Int?) : FilterChipUi

    data class MinRating(val value: Double) : FilterChipUi

    data class Sort(val sort: MovieSort) : FilterChipUi
}

internal fun MovieFilters.toChips(): List<FilterChipUi> = buildList {
    genres.forEach { add(FilterChipUi.Genre(it.id, it.name)) }
    if (yearFrom != null || yearTo != null) add(FilterChipUi.Years(yearFrom, yearTo))
    minRating?.let { add(FilterChipUi.MinRating(it)) }
    if (sort != MovieSort.POPULARITY) add(FilterChipUi.Sort(sort))
}

internal fun MovieFilters.without(chip: FilterChipUi): MovieFilters = when (chip) {
    is FilterChipUi.Genre -> copy(genres = genres.filterNot { it.id == chip.genreId })
    is FilterChipUi.Years -> copy(yearFrom = null, yearTo = null)
    is FilterChipUi.MinRating -> copy(minRating = null)
    is FilterChipUi.Sort -> copy(sort = MovieSort.POPULARITY)
}
