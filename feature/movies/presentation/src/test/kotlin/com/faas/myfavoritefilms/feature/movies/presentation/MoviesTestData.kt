package com.faas.myfavoritefilms.feature.movies.presentation

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.common.DomainException
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi

internal object MoviesTestData {
    val movies = List(6) { i ->
        MovieCardUi(
            id = i + 1,
            title = "Filme ${i + 1}",
            posterUrl = null,
            year = "20${10 + i}",
            rating = 7.0 + i / 10.0,
            ratingLabel = "7.$i",
            isFavorite = i == 0,
        )
    }

    private val notLoading = LoadState.NotLoading(endOfPaginationReached = false)

    fun content(list: List<MovieCardUi> = movies): PagingData<MovieCardUi> = PagingData.from(list)

    fun loading(): PagingData<MovieCardUi> = PagingData.empty(
        sourceLoadStates = LoadStates(refresh = LoadState.Loading, prepend = notLoading, append = notLoading),
    )

    fun error(error: DomainError = DomainError.Network): PagingData<MovieCardUi> = PagingData.empty(
        sourceLoadStates = LoadStates(
            refresh = LoadState.Error(DomainException(error)),
            prepend = notLoading,
            append = notLoading,
        ),
    )

    fun empty(): PagingData<MovieCardUi> = PagingData.empty(
        sourceLoadStates = LoadStates(
            refresh = LoadState.NotLoading(endOfPaginationReached = true),
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
            append = LoadState.NotLoading(endOfPaginationReached = true),
        ),
    )
}
