package com.faas.myfavoritefilms.feature.moviedetail.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.faas.myfavoritefilms.core.analytics.MovieSource
import com.faas.myfavoritefilms.core.analytics.favoriteToggledEvent
import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.testing.MainDispatcherRule
import com.faas.myfavoritefilms.core.testing.analytics.TestAnalytics
import com.faas.myfavoritefilms.feature.favorites.api.FakeFavorites
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail
import com.faas.myfavoritefilms.feature.moviedetail.domain.repository.MovieDetailRepository
import com.faas.myfavoritefilms.feature.moviedetail.domain.usecase.GetMovieDetailUseCase
import java.time.LocalDate
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MovieDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sample = MovieDetail(
        id = 603,
        title = "Matrix",
        tagline = null,
        overview = "",
        posterUrl = null,
        backdropUrl = null,
        releaseDate = LocalDate.of(1999, 3, 30),
        runtimeMinutes = 136,
        rating = 8.2,
        voteCount = 25_431,
        genres = emptyList(),
        infoSections = emptyList(),
    )

    private val detailRepository = object : MovieDetailRepository {
        var nextResult: AppResult<MovieDetail> = AppResult.Success(sample)
        var requestedIds = mutableListOf<Int>()
        override suspend fun getMovieDetail(movieId: Int): AppResult<MovieDetail> {
            requestedIds += movieId
            return nextResult
        }
    }
    private val favorites = FakeFavorites()
    private val analytics = TestAnalytics()

    private fun createViewModel() = MovieDetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf("movieId" to 603)),
        getMovieDetail = GetMovieDetailUseCase(detailRepository),
        observeIsFavorite = favorites.observeIsFavorite,
        toggleFavorite = favorites.toggleFavorite,
        analytics = analytics,
    )

    @Test
    fun `loads the movie from the route and maps it to UI`() = runTest {
        createViewModel().uiState.test {
            val state = expectMostRecentItem()
            assertTrue(state is MovieDetailUiState.Success)
            val movie = (state as MovieDetailUiState.Success).movie
            assertEquals(136, movie.runtimeMinutes)
            assertEquals("1999", movie.year)
            assertEquals(listOf(603), detailRepository.requestedIds)
        }
    }

    @Test
    fun `network error emits Error and retry recovers`() = runTest {
        detailRepository.nextResult = AppResult.Failure(DomainError.Network)
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(MovieDetailUiState.Error(DomainError.Network), expectMostRecentItem())

            detailRepository.nextResult = AppResult.Success(sample)
            viewModel.retry()

            assertTrue(expectMostRecentItem() is MovieDetailUiState.Success)
        }
    }

    @Test
    fun `favoriting updates state through the single source of truth`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(false, (expectMostRecentItem() as MovieDetailUiState.Success).isFavorite)

            viewModel.onToggleFavorite()

            assertEquals(true, (expectMostRecentItem() as MovieDetailUiState.Success).isFavorite)
            assertTrue(favorites.isFavorite(603))
        }
        assertEquals(listOf(favoriteToggledEvent(603, true, MovieSource.DETAIL)), analytics.events)
    }
}
