package com.faas.myfavoritefilms.feature.movies.domain.usecase

import androidx.paging.PagingData
import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeMoviesRepository : MoviesRepository {
    sealed interface Call {
        data object Popular : Call
        data class Search(val query: String) : Call
        data class Discover(val filters: MovieFilters) : Call
    }

    var lastCall: Call? = null
    var genresResult: AppResult<List<Genre>> = AppResult.Success(emptyList())
    var genresRequests = 0

    override fun popularMovies(): Flow<PagingData<Movie>> {
        lastCall = Call.Popular
        return flowOf(PagingData.empty())
    }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        lastCall = Call.Search(query)
        return flowOf(PagingData.empty())
    }

    override fun discoverMovies(filters: MovieFilters): Flow<PagingData<Movie>> {
        lastCall = Call.Discover(filters)
        return flowOf(PagingData.empty())
    }

    override suspend fun genres(): AppResult<List<Genre>> {
        genresRequests++
        return genresResult
    }
}
