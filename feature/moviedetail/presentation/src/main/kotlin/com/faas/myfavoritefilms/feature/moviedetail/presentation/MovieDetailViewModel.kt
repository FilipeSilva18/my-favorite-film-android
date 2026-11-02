package com.faas.myfavoritefilms.feature.moviedetail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faas.myfavoritefilms.core.analytics.Analytics
import com.faas.myfavoritefilms.core.analytics.MovieSource
import com.faas.myfavoritefilms.core.analytics.favoriteToggledEvent
import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.fold
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.api.ObserveIsFavoriteUseCase
import com.faas.myfavoritefilms.feature.favorites.api.ToggleFavoriteUseCase
import com.faas.myfavoritefilms.feature.moviedetail.domain.model.MovieDetail
import com.faas.myfavoritefilms.feature.moviedetail.domain.usecase.GetMovieDetailUseCase
import com.faas.myfavoritefilms.feature.moviedetail.domain.usecase.toFavoriteMovie
import com.faas.myfavoritefilms.feature.moviedetail.presentation.model.toUi
import com.faas.myfavoritefilms.feature.moviedetail.presentation.navigation.MOVIE_ID_ARG
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetail: GetMovieDetailUseCase,
    observeIsFavorite: ObserveIsFavoriteUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val analytics: Analytics,
) : ViewModel() {
    private val movieId: Int = checkNotNull(savedStateHandle.get<Int>(MOVIE_ID_ARG)) {
        "MovieDetailViewModel requires the '$MOVIE_ID_ARG' argument (MovieDetailRoute)"
    }

    private val reload = MutableStateFlow(0)

    private val detail: Flow<AppResult<MovieDetail>?> = reload.flatMapLatest {
        flow {
            emit(null)
            emit(getMovieDetail(movieId))
        }
    }

    private var loadedMovie: FavoriteMovie? = null

    val uiState: StateFlow<MovieDetailUiState> =
        combine(detail, observeIsFavorite(movieId)) { result, isFavorite ->
            when (result) {
                null -> MovieDetailUiState.Loading
                else -> result.fold(
                    onSuccess = { movie ->
                        loadedMovie = movie.toFavoriteMovie()
                        MovieDetailUiState.Success(movie.toUi(), isFavorite)
                    },
                    onFailure = { MovieDetailUiState.Error(it) },
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MovieDetailUiState.Loading,
        )

    fun retry() = reload.update { it + 1 }

    fun onToggleFavorite() {
        val movie = loadedMovie ?: return
        viewModelScope.launch {
            val isFavorite = toggleFavorite(movie)
            analytics.track(favoriteToggledEvent(movie.id, isFavorite, MovieSource.DETAIL))
        }
    }
}
