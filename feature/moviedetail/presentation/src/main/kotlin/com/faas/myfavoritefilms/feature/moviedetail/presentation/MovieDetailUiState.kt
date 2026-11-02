package com.faas.myfavoritefilms.feature.moviedetail.presentation

import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.feature.moviedetail.presentation.model.MovieDetailUi

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(val movie: MovieDetailUi, val isFavorite: Boolean) : MovieDetailUiState
    data class Error(val error: DomainError) : MovieDetailUiState
}
