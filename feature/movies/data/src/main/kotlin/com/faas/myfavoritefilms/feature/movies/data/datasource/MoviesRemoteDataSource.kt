package com.faas.myfavoritefilms.feature.movies.data.datasource

import com.faas.myfavoritefilms.feature.movies.data.mapper.DiscoverQuery
import com.faas.myfavoritefilms.feature.movies.data.remote.api.MoviesApi
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.GenreDto
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.MovieDto
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.PagedResponseDto
import javax.inject.Inject

internal class MoviesRemoteDataSource @Inject constructor(
    private val api: MoviesApi,
) {
    suspend fun popular(page: Int): PagedResponseDto<MovieDto> = api.getPopular(page)

    suspend fun search(query: String, page: Int): PagedResponseDto<MovieDto> = api.search(query = query, page = page)

    suspend fun discover(query: DiscoverQuery, page: Int): PagedResponseDto<MovieDto> = api.discover(
        page = page,
        sortBy = query.sortBy,
        withGenres = query.withGenres,
        releaseDateFrom = query.releaseDateFrom,
        releaseDateTo = query.releaseDateTo,
        minRating = query.minRating,
        minVoteCount = query.minVoteCount,
    )

    suspend fun genres(): List<GenreDto> = api.getGenres().genres
}
