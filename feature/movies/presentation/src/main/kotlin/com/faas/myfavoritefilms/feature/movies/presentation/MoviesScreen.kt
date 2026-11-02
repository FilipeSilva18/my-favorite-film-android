package com.faas.myfavoritefilms.feature.movies.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieSort
import com.faas.myfavoritefilms.feature.movies.presentation.model.AiSearchUiState
import com.faas.myfavoritefilms.feature.movies.presentation.model.FallbackReason
import com.faas.myfavoritefilms.feature.movies.presentation.model.FilterChipUi
import com.faas.myfavoritefilms.feature.movies.presentation.model.SearchMode
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.faas.myfavoritefilms.core.analytics.ScreenNames
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.designsystem.component.SearchField
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.di.daggerViewModel
import com.faas.myfavoritefilms.core.ui.EmptyContent
import com.faas.myfavoritefilms.core.ui.ErrorContent
import com.faas.myfavoritefilms.core.ui.TrackScreenView
import com.faas.myfavoritefilms.core.ui.asDomainError
import com.faas.myfavoritefilms.core.ui.movie.MovieCard
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.core.ui.movie.MovieGrid
import com.faas.myfavoritefilms.core.ui.movie.MovieGridSkeleton
import kotlinx.coroutines.flow.flowOf
import com.faas.myfavoritefilms.core.ui.R as CoreUiR

@Composable
internal fun MoviesScreen(
    onMovieClick: (movieId: Int) -> Unit,
    viewModel: MoviesViewModel = daggerViewModel(),
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchMode by viewModel.searchMode.collectAsStateWithLifecycle()
    val aiSearch by viewModel.aiSearch.collectAsStateWithLifecycle()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    TrackScreenView(ScreenNames.MOVIES)
    SearchResultsTracker(movies = movies, onSearchResultsShown = viewModel::onSearchResultsShown)

    MoviesContent(
        query = query,
        searchQuery = searchQuery,
        movies = movies,
        onQueryChange = viewModel::onQueryChange,
        searchMode = searchMode,
        aiSearch = aiSearch,
        onSearchModeChange = viewModel::onSearchModeChange,
        onSubmitSearch = viewModel::onSubmitSearch,
        onRemoveFilter = viewModel::onRemoveFilter,
        onMovieClick = { movieId ->
            viewModel.onMovieOpened(movieId)
            onMovieClick(movieId)
        },
        onToggleFavorite = viewModel::onToggleFavorite,
    )
}

@Composable
private fun SearchResultsTracker(
    movies: LazyPagingItems<MovieCardUi>,
    onSearchResultsShown: (Int) -> Unit,
) {
    val refresh = movies.loadState.refresh
    var wasLoading by remember { mutableStateOf(false) }
    LaunchedEffect(refresh) {
        when (refresh) {
            is LoadState.Loading -> wasLoading = true
            is LoadState.NotLoading -> if (wasLoading) {
                wasLoading = false
                onSearchResultsShown(movies.itemCount)
            }
            is LoadState.Error -> wasLoading = false
        }
    }
}

private sealed interface ScreenState {
    data object Loading : ScreenState
    data class Error(val error: DomainError) : ScreenState
    data object Empty : ScreenState
    data object Content : ScreenState
}

private fun LazyPagingItems<MovieCardUi>.screenState(): ScreenState {
    val refresh = loadState.refresh
    return when {
        itemCount > 0 -> ScreenState.Content
        refresh is LoadState.Loading -> ScreenState.Loading
        refresh is LoadState.Error -> ScreenState.Error(refresh.error.asDomainError())
        else -> ScreenState.Empty
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MoviesContent(
    query: String,
    searchQuery: String,
    movies: LazyPagingItems<MovieCardUi>,
    onQueryChange: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    onToggleFavorite: (MovieCardUi) -> Unit,
    modifier: Modifier = Modifier,
    searchMode: SearchMode = SearchMode.TITLE,
    aiSearch: AiSearchUiState = AiSearchUiState.Idle,
    onSearchModeChange: (SearchMode) -> Unit = {},
    onSubmitSearch: () -> Unit = {},
    onRemoveFilter: (FilterChipUi) -> Unit = {},
    gridState: LazyGridState = rememberLazyGridState(),
) {
    val isRefreshing = movies.loadState.refresh is LoadState.Loading

    LaunchedEffect(searchQuery, aiSearch) { gridState.scrollToItem(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),

        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
        topBar = {
            Column {
                TopAppBar(title = { Text(stringResource(R.string.movies_title)) })
                SearchModeSelector(
                    mode = searchMode,
                    onModeChange = onSearchModeChange,
                    modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 8.dp),
                )
                SearchField(
                    query = query,
                    onQueryChange = onQueryChange,
                    placeholder = stringResource(
                        if (searchMode == SearchMode.AI) R.string.movies_ai_placeholder else R.string.movies_search_placeholder,
                    ),
                    isLoading = (isRefreshing && query.isNotBlank()) || aiSearch == AiSearchUiState.Interpreting,
                    clearContentDescription = stringResource(CoreUiR.string.action_clear),
                    onSearch = onSubmitSearch,
                    modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 8.dp),
                )
                if (searchMode == SearchMode.AI) {
                    AiSearchStatus(
                        state = aiSearch,
                        onRemoveFilter = onRemoveFilter,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
            }
        },
    ) { padding ->
        Crossfade(
            targetState = movies.screenState(),
            modifier = Modifier.padding(padding),
            label = "moviesScreenState",
        ) { state ->
            when (state) {
                ScreenState.Loading -> MovieGridSkeleton()
                is ScreenState.Error -> ErrorContent(error = state.error, onRetry = movies::retry)
                ScreenState.Empty -> EmptyContent(
                    message = when {
                        aiSearch is AiSearchUiState.Applied -> stringResource(R.string.movies_ai_filters_empty)
                        searchQuery.isBlank() -> stringResource(R.string.movies_empty)
                        else -> stringResource(R.string.movies_search_empty, searchQuery)
                    },
                )
                ScreenState.Content -> PullToRefreshBox(
                    isRefreshing = isRefreshing && query.isBlank(),
                    onRefresh = movies::refresh,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    PagedMovieGrid(
                        movies = movies,
                        gridState = gridState,
                        onMovieClick = onMovieClick,
                        onToggleFavorite = onToggleFavorite,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchModeSelector(
    mode: SearchMode,
    onModeChange: (SearchMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        SearchMode.entries.forEachIndexed { index, item ->
            SegmentedButton(
                selected = mode == item,
                onClick = { onModeChange(item) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = SearchMode.entries.size),
            ) {
                Text(
                    stringResource(
                        when (item) {
                            SearchMode.TITLE -> R.string.movies_search_mode_title
                            SearchMode.AI -> R.string.movies_search_mode_ai
                        },
                    ),
                )
            }
        }
    }
}

@Composable
private fun AiSearchStatus(
    state: AiSearchUiState,
    onRemoveFilter: (FilterChipUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(targetState = state, modifier = modifier.fillMaxWidth(), label = "aiSearchStatus") { current ->
        when (current) {
            AiSearchUiState.Idle -> StatusText(stringResource(R.string.movies_ai_hint))
            AiSearchUiState.Interpreting -> StatusText(stringResource(R.string.movies_ai_interpreting))
            is AiSearchUiState.Applied -> LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
            ) {
                items(current.chips) { chip ->
                    val label = chip.label()
                    InputChip(
                        selected = true,
                        onClick = { onRemoveFilter(chip) },
                        label = { Text(label) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.movies_filter_remove, label),
                                modifier = Modifier.size(InputChipDefaults.IconSize),
                            )
                        },
                    )
                }
            }
            is AiSearchUiState.Fallback -> Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(horizontal = 12.dp),
            ) {
                Text(
                    text = stringResource(
                        when (current.reason) {
                            FallbackReason.NOT_UNDERSTOOD -> R.string.movies_ai_not_understood
                            FallbackReason.UNAVAILABLE -> R.string.movies_ai_unavailable
                        },
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun StatusText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

@Composable
private fun FilterChipUi.label(): String = when (this) {
    is FilterChipUi.Genre -> name
    is FilterChipUi.Years -> when {
        from != null && to != null && from == to -> from.toString()
        from != null && to != null -> stringResource(R.string.movies_filter_years_range, from, to)
        from != null -> stringResource(R.string.movies_filter_years_from, from)
        else -> stringResource(R.string.movies_filter_years_until, to ?: 0)
    }
    is FilterChipUi.MinRating -> stringResource(R.string.movies_filter_min_rating, formatRatingLabel(value))
    is FilterChipUi.Sort -> stringResource(
        when (sort) {
            MovieSort.RATING -> R.string.movies_filter_sort_rating
            MovieSort.NEWEST -> R.string.movies_filter_sort_newest
            MovieSort.POPULARITY -> R.string.movies_filter_sort_popularity
        },
    )
}

private fun formatRatingLabel(value: Double): String =
    if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()

@Composable
private fun PagedMovieGrid(
    movies: LazyPagingItems<MovieCardUi>,
    gridState: LazyGridState,
    onMovieClick: (Int) -> Unit,
    onToggleFavorite: (MovieCardUi) -> Unit,
) {
    MovieGrid(state = gridState) {
        items(
            count = movies.itemCount,

            key = { index -> "${movies.peek(index)?.id}-$index" },
            contentType = { "movie" },
        ) { index ->
            movies[index]?.let { movie ->
                MovieCard(
                    movie = movie,
                    onClick = { onMovieClick(movie.id) },
                    onToggleFavorite = { onToggleFavorite(movie) },
                    modifier = Modifier.animateItem(),
                )
            }
        }

        when (movies.loadState.append) {
            is LoadState.Loading -> item(span = { GridItemSpan(maxLineSpan) }, contentType = "footer") {
                Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error -> item(span = { GridItemSpan(maxLineSpan) }, contentType = "footer") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TextButton(onClick = movies::retry) {
                        Text(stringResource(CoreUiR.string.action_retry))
                    }
                }
            }
            is LoadState.NotLoading -> Unit
        }
    }
}

private val previewMovies = List(6) { i ->
    MovieCardUi(
        id = i,
        title = "Filme de exemplo ${i + 1}",
        posterUrl = null,
        year = "202$i",
        rating = 7.0 + i / 10.0,
        ratingLabel = "7.$i",
        isFavorite = i % 2 == 0,
    )
}

@Preview(showBackground = true)
@Composable
private fun MoviesContentPreview() {
    val movies = flowOf(PagingData.from(previewMovies)).collectAsLazyPagingItems()
    MoviesTheme {
        MoviesContent(
            query = "",
            searchQuery = "",
            movies = movies,
            onQueryChange = {},
            onMovieClick = {},
            onToggleFavorite = {},
        )
    }
}
