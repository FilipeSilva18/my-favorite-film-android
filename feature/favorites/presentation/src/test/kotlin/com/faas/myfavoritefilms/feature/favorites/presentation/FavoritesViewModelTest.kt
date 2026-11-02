package com.faas.myfavoritefilms.feature.favorites.presentation

import app.cash.turbine.test
import com.faas.myfavoritefilms.core.analytics.MovieSource
import com.faas.myfavoritefilms.core.analytics.favoriteToggledEvent
import com.faas.myfavoritefilms.core.analytics.movieOpenedEvent
import com.faas.myfavoritefilms.core.testing.MainDispatcherRule
import com.faas.myfavoritefilms.core.testing.analytics.TestAnalytics
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.domain.FakeFavoritesRepository
import com.faas.myfavoritefilms.feature.favorites.domain.usecase.ObserveFavoritesUseCase
import com.faas.myfavoritefilms.feature.favorites.domain.usecase.ToggleFavoriteUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeFavoritesRepository()
    private val analytics = TestAnalytics()

    private fun createViewModel() = FavoritesViewModel(
        observeFavorites = ObserveFavoritesUseCase(repository),
        toggleFavorite = ToggleFavoriteUseCaseImpl(repository),
        analytics = analytics,
    )

    @Test
    fun `no favorites shows empty state`() = runTest {
        createViewModel().uiState.test {
            assertEquals(FavoritesUiState.Empty, expectMostRecentItem())
        }
    }

    @Test
    fun `lists favorites and removing one updates the list`() = runTest {
        repository.add(FavoriteMovie(603, "Matrix", null, 8.2))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val content = expectMostRecentItem() as FavoritesUiState.Content
            assertEquals(listOf("Matrix"), content.movies.map { it.title })
            assertEquals(listOf("8.2"), content.movies.map { it.ratingLabel })

            viewModel.onToggleFavorite(content.movies.single())

            assertEquals(FavoritesUiState.Empty, expectMostRecentItem())
        }
        assertEquals(listOf(favoriteToggledEvent(603, false, MovieSource.FAVORITES)), analytics.events)
    }

    @Test
    fun `opening a movie from favorites tracks the source`() {
        createViewModel().onMovieOpened(603)

        assertEquals(listOf(movieOpenedEvent(603, MovieSource.FAVORITES)), analytics.events)
    }
}
