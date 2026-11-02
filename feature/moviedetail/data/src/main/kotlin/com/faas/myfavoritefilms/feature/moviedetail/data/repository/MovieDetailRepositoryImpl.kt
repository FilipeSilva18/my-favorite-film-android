package com.faas.myfavoritefilms.feature.moviedetail.data.repository

import com.faas.myfavoritefilms.core.analytics.ErrorReporter
import com.faas.myfavoritefilms.core.analytics.reportIfUnexpected
import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.network.safeApiCall
import com.faas.myfavoritefilms.core.network.sdui.SduiParser
import com.faas.myfavoritefilms.feature.moviedetail.data.datasource.MovieDetailRemoteDataSource
import com.faas.myfavoritefilms.feature.moviedetail.data.mapper.toDomain
import com.faas.myfavoritefilms.feature.moviedetail.data.sdui.MovieDetailLayoutService
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail
import com.faas.myfavoritefilms.feature.moviedetail.domain.repository.MovieDetailRepository
import javax.inject.Inject

internal class MovieDetailRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieDetailRemoteDataSource,
    private val layoutService: MovieDetailLayoutService,
    private val sduiParser: SduiParser,
    private val errorReporter: ErrorReporter,
) : MovieDetailRepository {
    override suspend fun getMovieDetail(movieId: Int): AppResult<MovieDetail> = safeApiCall(
        onFailure = { cause, error -> errorReporter.reportIfUnexpected(cause, error, origin = "movie_detail") },
    ) {
        val dto = remoteDataSource.movieDetail(movieId)

        val sections = sduiParser.parse(layoutService.layoutFor(dto))
        dto.toDomain(infoSections = sections)
    }
}
