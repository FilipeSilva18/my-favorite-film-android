package com.faas.myfavoritefilms.feature.moviedetail.domain.usecase

import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail
import com.faas.myfavoritefilms.feature.moviedetail.domain.repository.MovieDetailRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMovieDetailUseCaseTest {

    @Test
    fun `forwards the id and returns the repository result`() = runTest {
        var requestedId: Int? = null
        val repository = object : MovieDetailRepository {
            override suspend fun getMovieDetail(movieId: Int): AppResult<MovieDetail> {
                requestedId = movieId
                return AppResult.Failure(DomainError.NotFound)
            }
        }

        val result = GetMovieDetailUseCase(repository)(603)

        assertEquals(603, requestedId)
        assertEquals(AppResult.Failure(DomainError.NotFound), result)
    }
}
