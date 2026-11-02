package com.faas.myfavoritefilms.feature.moviedetail.domain.usecase

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail
import com.faas.myfavoritefilms.feature.moviedetail.domain.repository.MovieDetailRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieDetailRepository,
) {
    suspend operator fun invoke(movieId: Int): AppResult<MovieDetail> =
        repository.getMovieDetail(movieId)
}
