package com.faas.myfavoritefilms.feature.moviedetail.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faas.myfavoritefilms.core.analytics.ScreenNames
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.common.sdui.KeyValueItem
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.core.designsystem.component.FavoriteIconButton
import com.faas.myfavoritefilms.core.designsystem.component.LabelChips
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.di.daggerViewModel
import com.faas.myfavoritefilms.core.ui.ErrorContent
import com.faas.myfavoritefilms.core.ui.TrackScreenView
import com.faas.myfavoritefilms.core.ui.sdui.sduiItems
import com.faas.myfavoritefilms.feature.moviedetail.presentation.component.MovieDetailHeader
import com.faas.myfavoritefilms.feature.moviedetail.presentation.component.MovieDetailSkeleton
import com.faas.myfavoritefilms.feature.moviedetail.presentation.model.MovieDetailUi
import com.faas.myfavoritefilms.core.ui.R as CoreUiR

@Composable
internal fun MovieDetailScreen(
    onBack: () -> Unit,
    viewModel: MovieDetailViewModel = daggerViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TrackScreenView(ScreenNames.MOVIE_DETAIL)
    MovieDetailContent(
        state = state,
        onBack = onBack,
        onRetry = viewModel::retry,
        onToggleFavorite = viewModel::onToggleFavorite,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MovieDetailContent(
    state: MovieDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    if (state is MovieDetailUiState.Success) {
                        Text(state.movie.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(CoreUiR.string.action_back),
                        )
                    }
                },
                actions = {
                    if (state is MovieDetailUiState.Success) {
                        FavoriteIconButton(
                            isFavorite = state.isFavorite,
                            onClick = onToggleFavorite,
                            contentDescription = stringResource(
                                if (state.isFavorite) {
                                    CoreUiR.string.movie_favorite_remove
                                } else {
                                    CoreUiR.string.movie_favorite_add
                                },
                            ),
                        )
                    }
                },
            )
        },
    ) { padding ->
        AnimatedContent(
            targetState = state,

            contentKey = { it::class },
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier.padding(padding),
            label = "movieDetailState",
        ) { target ->
            when (target) {
                MovieDetailUiState.Loading -> MovieDetailSkeleton()
                is MovieDetailUiState.Error -> ErrorContent(error = target.error, onRetry = onRetry)
                is MovieDetailUiState.Success -> MovieDetailBody(movie = target.movie)
            }
        }
    }
}

@Composable
private fun MovieDetailBody(movie: MovieDetailUi) {
    val sectionModifier = Modifier.padding(horizontal = 16.dp)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item(key = "header", contentType = "header") { MovieDetailHeader(movie) }

        if (movie.genres.isNotEmpty()) {
            item(key = "genres", contentType = "genres") {
                LabelChips(labels = movie.genres, modifier = sectionModifier)
            }
        }

        if (movie.overview.isNotBlank()) {
            item(key = "overview", contentType = "overview") {
                Column(sectionModifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.detail_overview), style = MaterialTheme.typography.titleMedium)
                    Text(movie.overview, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        sduiItems(components = movie.infoSections, itemModifier = sectionModifier)
    }
}

private val previewMovie = MovieDetailUi(
    id = 603,
    title = "Matrix",
    tagline = "Bem-vindo ao mundo real.",
    overview = "Um hacker descobre que a realidade é uma simulação.",
    posterUrl = null,
    backdropUrl = null,
    releaseDateLabel = "30 de mar. de 1999",
    year = "1999",
    runtimeMinutes = 136,
    ratingLabel = "8.2",
    voteCountLabel = "25.431",
    genres = listOf("Ação", "Ficção científica"),
    infoSections = listOf(
        SduiComponent.KeyValueList(
            title = "Ficha técnica",
            items = listOf(
                KeyValueItem("Título original", "The Matrix", "info"),
                KeyValueItem("Orçamento", "$63,000,000", "money"),
            ),
        ),
        SduiComponent.ChipGroup("Produtoras", listOf("Warner Bros.", "Village Roadshow")),
    ),
)

@Preview(showBackground = true)
@Composable
private fun MovieDetailSuccessPreview() {
    MoviesTheme {
        MovieDetailContent(
            state = MovieDetailUiState.Success(previewMovie, isFavorite = true),
            onBack = {},
            onRetry = {},
            onToggleFavorite = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailLoadingPreview() {
    MoviesTheme {
        MovieDetailContent(state = MovieDetailUiState.Loading, onBack = {}, onRetry = {}, onToggleFavorite = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailErrorPreview() {
    MoviesTheme {
        MovieDetailContent(
            state = MovieDetailUiState.Error(DomainError.Network),
            onBack = {},
            onRetry = {},
            onToggleFavorite = {},
        )
    }
}
