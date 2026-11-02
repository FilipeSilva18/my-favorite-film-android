package com.faas.myfavoritefilms.feature.movies.domain.repository

import androidx.paging.PagingData
import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun popularMovies(): Flow<PagingData<Movie>>

    fun searchMovies(query: String): Flow<PagingData<Movie>>

    fun discoverMovies(filters: MovieFilters): Flow<PagingData<Movie>>

    suspend fun genres(): AppResult<List<Genre>>
}
