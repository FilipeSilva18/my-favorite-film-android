package com.faas.myfavoritefilms.feature.favorites.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faas.myfavoritefilms.core.analytics.ScreenNames
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.di.daggerViewModel
import com.faas.myfavoritefilms.core.ui.EmptyContent
import com.faas.myfavoritefilms.core.ui.TrackScreenView
import com.faas.myfavoritefilms.core.ui.movie.MovieCard
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.core.ui.movie.MovieGrid
import com.faas.myfavoritefilms.core.ui.movie.MovieGridSkeleton

@Composable
internal fun FavoritesScreen(
    onMovieClick: (movieId: Int) -> Unit,
    viewModel: FavoritesViewModel = daggerViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TrackScreenView(ScreenNames.FAVORITES)
    FavoritesContent(
        state = state,
        onMovieClick = { movieId ->
            viewModel.onMovieOpened(movieId)
            onMovieClick(movieId)
        },
        onToggleFavorite = viewModel::onToggleFavorite,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesContent(
    state: FavoritesUiState,
    onMovieClick: (Int) -> Unit,
    onToggleFavorite: (MovieCardUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),

        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.favorites_title)) }) },
    ) { padding ->
        Crossfade(
            targetState = state,
            modifier = Modifier.padding(padding),
            label = "favoritesState",
        ) { target ->
            when (target) {
                FavoritesUiState.Loading -> MovieGridSkeleton()
                FavoritesUiState.Empty -> EmptyContent(message = stringResource(R.string.favorites_empty))
                is FavoritesUiState.Content -> MovieGrid {
                    items(items = target.movies, key = { it.id }, contentType = { "movie" }) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) },
                            onToggleFavorite = { onToggleFavorite(movie) },
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesContentPreview() {
    MoviesTheme {
        FavoritesContent(
            state = FavoritesUiState.Content(
                List(4) { MovieCardUi(it, "Favorito ${it + 1}", null, null, 8.0, "8.0", isFavorite = true) },
            ),
            onMovieClick = {},
            onToggleFavorite = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesEmptyPreview() {
    MoviesTheme {
        FavoritesContent(state = FavoritesUiState.Empty, onMovieClick = {}, onToggleFavorite = {})
    }
}
