package com.faas.myfavoritefilms.feature.favorites.presentation

import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Content(val movies: List<MovieCardUi>) : FavoritesUiState
}
