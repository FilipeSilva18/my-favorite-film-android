package com.faas.myfavoritefilms.feature.movies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faas.myfavoritefilms.core.analytics.ErrorReporter
import com.faas.myfavoritefilms.core.analytics.reportIfUnexpected
import com.faas.myfavoritefilms.core.common.DomainException
import com.faas.myfavoritefilms.core.network.toDomainError
import com.faas.myfavoritefilms.feature.movies.data.mapper.toDomain
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.MovieDto
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.PagedResponseDto
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie
import kotlin.coroutines.cancellation.CancellationException

internal class MoviesPagingSource(
    private val errorReporter: ErrorReporter,
    private val origin: String,
    private val fetch: suspend (page: Int) -> PagedResponseDto<MovieDto>,
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val response = fetch(page)
            val lastPage = minOf(response.totalPages, TMDB_MAX_PAGE)
            LoadResult.Page(
                data = response.results.map(MovieDto::toDomain),
                prevKey = if (page == FIRST_PAGE) null else page - 1,
                nextKey = if (page >= lastPage) null else page + 1,
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val error = e.toDomainError()
            errorReporter.reportIfUnexpected(e, error, origin)
            LoadResult.Error(DomainException(error))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }

    internal companion object {
        const val FIRST_PAGE = 1
        const val TMDB_MAX_PAGE = 500
    }
}
