package com.faas.myfavoritefilms.feature.moviedetail.domain.repository

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail

interface MovieDetailRepository {
    suspend fun getMovieDetail(movieId: Int): AppResult<MovieDetail>
}
