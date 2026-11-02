package com.faas.myfavoritefilms.feature.movies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.faas.myfavoritefilms.core.analytics.ErrorReporter
import com.faas.myfavoritefilms.core.analytics.reportIfUnexpected
import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.network.safeApiCall
import com.faas.myfavoritefilms.feature.movies.data.datasource.MoviesRemoteDataSource
import com.faas.myfavoritefilms.feature.movies.data.mapper.toDiscoverQuery
import com.faas.myfavoritefilms.feature.movies.data.mapper.toDomain
import com.faas.myfavoritefilms.feature.movies.data.paging.MoviesPagingSource
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.MovieDto
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.PagedResponseDto
import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

internal class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val errorReporter: ErrorReporter,
) : MoviesRepository {
    private val genresMutex = Mutex()
    private val cachedGenres = mutableMapOf<String, List<Genre>>()

    override fun popularMovies(): Flow<PagingData<Movie>> =
        pager(origin = "movies_popular") { page -> remoteDataSource.popular(page) }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> =
        pager(origin = "movies_search") { page -> remoteDataSource.search(query, page) }

    override fun discoverMovies(filters: MovieFilters): Flow<PagingData<Movie>> {
        val query = filters.toDiscoverQuery(today = LocalDate.now())
        return pager(origin = "movies_discover") { page -> remoteDataSource.discover(query, page) }
    }

    override suspend fun genres(): AppResult<List<Genre>> = genresMutex.withLock {
        val language = Locale.getDefault().toLanguageTag()
        cachedGenres[language]?.let { return@withLock AppResult.Success(it) }
        safeApiCall(
            onFailure = { cause, error -> errorReporter.reportIfUnexpected(cause, error, origin = "movies_genres") },
        ) {
            remoteDataSource.genres().map { it.toDomain() }
        }.also { result ->
            if (result is AppResult.Success) cachedGenres[language] = result.data
        }
    }

    private fun pager(
        origin: String,
        fetch: suspend (Int) -> PagedResponseDto<MovieDto>,
    ): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = TMDB_PAGE_SIZE,
                initialLoadSize = TMDB_PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { MoviesPagingSource(errorReporter, origin, fetch) },
        ).flow

    private companion object {
        const val TMDB_PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 6
    }
}
