package com.faas.myfavoritefilms.feature.moviedetail.data.datasource

import com.faas.myfavoritefilms.feature.moviedetail.data.remote.api.MovieDetailApi
import com.faas.myfavoritefilms.feature.moviedetail.data.remote.dto.MovieDetailDto
import javax.inject.Inject

internal class MovieDetailRemoteDataSource @Inject constructor(
    private val api: MovieDetailApi,
) {
    suspend fun movieDetail(movieId: Int): MovieDetailDto = api.getMovieDetail(movieId)
}
