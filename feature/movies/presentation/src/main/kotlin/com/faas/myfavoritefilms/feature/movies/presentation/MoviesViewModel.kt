package com.faas.myfavoritefilms.feature.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.faas.myfavoritefilms.core.analytics.AiSearchOutcome
import com.faas.myfavoritefilms.core.analytics.Analytics
import com.faas.myfavoritefilms.core.analytics.MovieSource
import com.faas.myfavoritefilms.core.analytics.aiSearchEvent
import com.faas.myfavoritefilms.core.analytics.favoriteToggledEvent
import com.faas.myfavoritefilms.core.analytics.movieOpenedEvent
import com.faas.myfavoritefilms.core.analytics.searchEvent
import com.faas.myfavoritefilms.core.analytics.searchResultsEvent
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.feature.favorites.api.ObserveFavoriteIdsUseCase
import com.faas.myfavoritefilms.feature.favorites.api.ToggleFavoriteUseCase
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.model.QueryInterpretation
import com.faas.myfavoritefilms.feature.movies.domain.usecase.GetMoviesByFiltersUseCase
import com.faas.myfavoritefilms.feature.movies.domain.usecase.GetMoviesUseCase
import com.faas.myfavoritefilms.feature.movies.domain.usecase.InterpretMovieQueryUseCase
import com.faas.myfavoritefilms.feature.movies.presentation.model.AiSearchUiState
import com.faas.myfavoritefilms.feature.movies.presentation.model.FallbackReason
import com.faas.myfavoritefilms.feature.movies.presentation.model.FilterChipUi
import com.faas.myfavoritefilms.feature.movies.presentation.model.SearchMode
import com.faas.myfavoritefilms.feature.movies.presentation.model.toCardUi
import com.faas.myfavoritefilms.feature.movies.presentation.model.toChips
import com.faas.myfavoritefilms.feature.movies.presentation.model.toFavoriteMovie
import com.faas.myfavoritefilms.feature.movies.presentation.model.without
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MoviesViewModel @Inject constructor(
    getMovies: GetMoviesUseCase,
    getMoviesByFilters: GetMoviesByFiltersUseCase,
    observeFavoriteIds: ObserveFavoriteIdsUseCase,
    private val interpretMovieQuery: InterpretMovieQueryUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val analytics: Analytics,
) : ViewModel() {

    private sealed interface AiState {
        data object Idle : AiState
        data object Interpreting : AiState
        data class Applied(val filters: MovieFilters) : AiState
        data class Fallback(val text: String, val reason: FallbackReason) : AiState
    }

    private sealed interface MoviesSource {
        data class Text(val query: String) : MoviesSource
        data class Filters(val filters: MovieFilters) : MoviesSource
    }

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchMode = MutableStateFlow(SearchMode.TITLE)
    val searchMode: StateFlow<SearchMode> = _searchMode.asStateFlow()

    private val aiState = MutableStateFlow<AiState>(AiState.Idle)
    private var interpretJob: Job? = null

    val aiSearch: StateFlow<AiSearchUiState> = aiState
        .map { it.toUi() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AiSearchUiState.Idle)

    val searchQuery: StateFlow<String> = _query
        .debounce { if (it.isBlank()) 0L else SEARCH_DEBOUNCE_MS }
        .map(String::trim)
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    private val source: StateFlow<MoviesSource> =
        combine(_searchMode, searchQuery, aiState) { mode, query, ai ->
            when (mode) {
                SearchMode.TITLE -> MoviesSource.Text(query)
                SearchMode.AI -> when (ai) {
                    AiState.Idle -> MoviesSource.Text("")
                    AiState.Interpreting -> null
                    is AiState.Applied -> MoviesSource.Filters(ai.filters)
                    is AiState.Fallback -> MoviesSource.Text(ai.text)
                }
            }
        }
            .filterNotNull()
            .distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.Eagerly, MoviesSource.Text(""))

    val movies: Flow<PagingData<MovieCardUi>> = source
        .flatMapLatest { source ->
            when (source) {
                is MoviesSource.Text -> getMovies(source.query)
                is MoviesSource.Filters -> getMoviesByFilters(source.filters)
            }
        }
        .cachedIn(viewModelScope)
        .combine(observeFavoriteIds()) { paging, favoriteIds ->
            paging.map { movie -> movie.toCardUi(isFavorite = movie.id in favoriteIds) }
        }

    private val isTitleSearchActive: Boolean
        get() = _searchMode.value == SearchMode.TITLE &&
            searchQuery.value.length >= GetMoviesUseCase.MIN_QUERY_LENGTH

    private val currentSource: MovieSource
        get() = when {
            _searchMode.value == SearchMode.AI && aiState.value is AiState.Applied -> MovieSource.AI_SEARCH
            _searchMode.value == SearchMode.AI && aiState.value is AiState.Fallback -> MovieSource.SEARCH
            isTitleSearchActive -> MovieSource.SEARCH
            else -> MovieSource.POPULAR
        }

    init {
        searchQuery
            .filter { _searchMode.value == SearchMode.TITLE && it.length >= GetMoviesUseCase.MIN_QUERY_LENGTH }
            .onEach { analytics.track(searchEvent(it)) }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(value: String) {
        _query.value = value
        if (value.isBlank() && _searchMode.value == SearchMode.AI) resetAiSearch()
    }

    fun onSearchModeChange(mode: SearchMode) {
        if (_searchMode.value == mode) return
        resetAiSearch()
        _searchMode.value = mode
    }

    fun onSubmitSearch() {
        if (_searchMode.value != SearchMode.AI) return
        val description = _query.value.trim()
        if (description.isEmpty()) return

        interpretJob?.cancel()
        interpretJob = viewModelScope.launch {
            aiState.value = AiState.Interpreting
            val (state, outcome) = when (val interpretation = interpretMovieQuery(description)) {
                is QueryInterpretation.Understood ->
                    AiState.Applied(interpretation.filters) to AiSearchOutcome.UNDERSTOOD
                QueryInterpretation.NotUnderstood ->
                    AiState.Fallback(description, FallbackReason.NOT_UNDERSTOOD) to AiSearchOutcome.NOT_UNDERSTOOD
                is QueryInterpretation.Unavailable ->
                    AiState.Fallback(description, FallbackReason.UNAVAILABLE) to AiSearchOutcome.UNAVAILABLE
            }
            aiState.value = state
            val filterCount = (state as? AiState.Applied)?.filters?.toChips()?.size ?: 0
            analytics.track(aiSearchEvent(description, outcome, filterCount))
        }
    }

    fun onRemoveFilter(chip: FilterChipUi) {
        val applied = aiState.value as? AiState.Applied ?: return
        val updated = applied.filters.without(chip)
        aiState.value = if (updated.hasCriteria) AiState.Applied(updated) else AiState.Idle
    }

    fun onSearchResultsShown(firstPageCount: Int) {
        if (isTitleSearchActive) {
            analytics.track(searchResultsEvent(searchQuery.value, firstPageCount))
        }
    }

    fun onMovieOpened(movieId: Int) {
        analytics.track(movieOpenedEvent(movieId, currentSource))
    }

    fun onToggleFavorite(item: MovieCardUi) {
        val source = currentSource
        viewModelScope.launch {
            val isFavorite = toggleFavorite(item.toFavoriteMovie())
            analytics.track(favoriteToggledEvent(item.id, isFavorite, source))
        }
    }

    private fun resetAiSearch() {
        interpretJob?.cancel()
        interpretJob = null
        aiState.value = AiState.Idle
    }

    private fun AiState.toUi(): AiSearchUiState = when (this) {
        AiState.Idle -> AiSearchUiState.Idle
        AiState.Interpreting -> AiSearchUiState.Interpreting
        is AiState.Applied -> AiSearchUiState.Applied(filters.toChips())
        is AiState.Fallback -> AiSearchUiState.Fallback(reason)
    }

    internal companion object {
        const val SEARCH_DEBOUNCE_MS = 400L
    }
}
