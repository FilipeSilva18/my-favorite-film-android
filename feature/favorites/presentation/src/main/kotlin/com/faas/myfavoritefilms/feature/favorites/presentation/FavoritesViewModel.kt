package com.faas.myfavoritefilms.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faas.myfavoritefilms.core.analytics.Analytics
import com.faas.myfavoritefilms.core.analytics.MovieSource
import com.faas.myfavoritefilms.core.analytics.favoriteToggledEvent
import com.faas.myfavoritefilms.core.analytics.movieOpenedEvent
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.feature.favorites.api.ToggleFavoriteUseCase
import com.faas.myfavoritefilms.feature.favorites.domain.usecase.ObserveFavoritesUseCase
import com.faas.myfavoritefilms.feature.favorites.presentation.model.toCardUi
import com.faas.myfavoritefilms.feature.favorites.presentation.model.toFavoriteMovie
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel @Inject constructor(
    observeFavorites: ObserveFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val analytics: Analytics,
) : ViewModel() {
    val uiState: StateFlow<FavoritesUiState> = observeFavorites()
        .map { favorites ->
            if (favorites.isEmpty()) {
                FavoritesUiState.Empty
            } else {
                FavoritesUiState.Content(favorites.map { it.toCardUi() })
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState.Loading,
        )

    fun onMovieOpened(movieId: Int) {
        analytics.track(movieOpenedEvent(movieId, MovieSource.FAVORITES))
    }

    fun onToggleFavorite(item: MovieCardUi) {
        viewModelScope.launch {
            val isFavorite = toggleFavorite(item.toFavoriteMovie())
            analytics.track(favoriteToggledEvent(item.id, isFavorite, MovieSource.FAVORITES))
        }
    }
}
