package com.faas.myfavoritefilms.feature.movies.presentation

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.faas.myfavoritefilms.core.analytics.AiSearchOutcome
import com.faas.myfavoritefilms.core.analytics.AnalyticsEvents
import com.faas.myfavoritefilms.core.analytics.MovieSource
import com.faas.myfavoritefilms.core.analytics.aiSearchEvent
import com.faas.myfavoritefilms.core.analytics.favoriteToggledEvent
import com.faas.myfavoritefilms.core.analytics.movieOpenedEvent
import com.faas.myfavoritefilms.core.analytics.searchEvent
import com.faas.myfavoritefilms.core.analytics.searchResultsEvent
import com.faas.myfavoritefilms.core.common.AppResult
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.testing.MainDispatcherRule
import com.faas.myfavoritefilms.core.testing.analytics.TestAnalytics
import com.faas.myfavoritefilms.feature.favorites.api.FakeFavorites
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.movies.domain.interpreter.MovieQueryInterpreter
import com.faas.myfavoritefilms.feature.movies.domain.model.Genre
import com.faas.myfavoritefilms.feature.movies.domain.model.Movie
import com.faas.myfavoritefilms.feature.movies.domain.model.MovieFilters
import com.faas.myfavoritefilms.feature.movies.domain.repository.MoviesRepository
import com.faas.myfavoritefilms.feature.movies.domain.usecase.GetMoviesByFiltersUseCase
import com.faas.myfavoritefilms.feature.movies.domain.usecase.GetMoviesUseCase
import com.faas.myfavoritefilms.feature.movies.domain.usecase.InterpretMovieQueryUseCase
import com.faas.myfavoritefilms.feature.movies.presentation.model.AiSearchUiState
import com.faas.myfavoritefilms.feature.movies.presentation.model.FallbackReason
import com.faas.myfavoritefilms.feature.movies.presentation.model.FilterChipUi
import com.faas.myfavoritefilms.feature.movies.presentation.model.SearchMode
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val popular = listOf(
        Movie(1, "Matrix", "", null, LocalDate.of(1999, 3, 30), 8.2),
        Movie(2, "Duna", "", null, null, 7.84),
    )
    private val searchResult = listOf(Movie(3, "Matrix Reloaded", "", null, null, 7.0))

    private val discoverResult = listOf(Movie(4, "Fogo contra Fogo", "", null, null, 7.9))
    private val crime = Genre(80, "Crime")
    private val thriller = Genre(53, "Thriller")

    private val moviesRepository = object : MoviesRepository {
        val searches = mutableListOf<String>()
        val discovered = mutableListOf<MovieFilters>()
        override fun popularMovies(): Flow<PagingData<Movie>> = pagerOf(popular)
        override fun searchMovies(query: String): Flow<PagingData<Movie>> {
            searches += query
            return pagerOf(searchResult)
        }
        override fun discoverMovies(filters: MovieFilters): Flow<PagingData<Movie>> {
            discovered += filters
            return pagerOf(discoverResult)
        }
        override suspend fun genres(): AppResult<List<Genre>> = AppResult.Success(listOf(crime, thriller))
    }
    private val interpreter = object : MovieQueryInterpreter {
        var result: AppResult<MovieFilters> = AppResult.Success(MovieFilters())
        override suspend fun interpret(description: String, genres: List<Genre>): AppResult<MovieFilters> = result
    }
    private val favorites = FakeFavorites()
    private val analytics = TestAnalytics()

    private fun createViewModel() = MoviesViewModel(
        getMovies = GetMoviesUseCase(moviesRepository),
        getMoviesByFilters = GetMoviesByFiltersUseCase(moviesRepository),
        interpretMovieQuery = InterpretMovieQueryUseCase(moviesRepository, interpreter),
        observeFavoriteIds = favorites.observeFavoriteIds,
        toggleFavorite = favorites.toggleFavorite,
        analytics = analytics,
    )

    @Test
    fun `without search shows popular movies with formatted year and rating`() = runTest {
        val items = createViewModel().movies.asSnapshot()

        assertEquals(listOf("1999", null), items.map { it.year })
        assertEquals(listOf("8.2", "7.8"), items.map { it.ratingLabel })
    }

    @Test
    fun `marks saved movies as favorite`() = runTest {
        favorites.add(FavoriteMovie(2, "Duna", null, 7.84))

        val items = createViewModel().movies.asSnapshot()

        assertEquals(listOf(false, true), items.map { it.isFavorite })
    }

    @Test
    fun `onToggleFavorite saves the movie`() = runTest {
        val viewModel = createViewModel()
        val matrix = viewModel.movies.asSnapshot().first()

        viewModel.onToggleFavorite(matrix)

        assertTrue(favorites.isFavorite(1))
    }

    @Test
    fun `search only fires after debounce with normalized text`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onQueryChange("  matr")
        viewModel.onQueryChange("  matrix ")
        advanceTimeBy(MoviesViewModel.SEARCH_DEBOUNCE_MS + 1)

        assertEquals("matrix", viewModel.searchQuery.value)
        assertEquals(listOf("Matrix Reloaded"), viewModel.movies.asSnapshot().map { it.title })
        assertEquals(listOf("matrix"), moviesRepository.searches)
    }

    @Test
    fun `effective search tracks a single search event with the normalized term`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onQueryChange("m")
        viewModel.onQueryChange("matr")
        viewModel.onQueryChange(" matrix ")
        advanceTimeBy(MoviesViewModel.SEARCH_DEBOUNCE_MS + 1)

        assertEquals(listOf(searchEvent("matrix")), analytics.eventsNamed(AnalyticsEvents.SEARCH))
    }

    @Test
    fun `search results are only tracked while searching`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onSearchResultsShown(20)
        viewModel.onQueryChange("matrix")
        advanceTimeBy(MoviesViewModel.SEARCH_DEBOUNCE_MS + 1)
        viewModel.onSearchResultsShown(3)

        assertEquals(listOf(searchResultsEvent("matrix", 3)), analytics.eventsNamed(AnalyticsEvents.SEARCH_RESULTS))
    }

    @Test
    fun `opening a movie tracks popular or search source`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onMovieOpened(1)
        viewModel.onQueryChange("matrix")
        advanceTimeBy(MoviesViewModel.SEARCH_DEBOUNCE_MS + 1)
        viewModel.onMovieOpened(3)

        assertEquals(
            listOf(movieOpenedEvent(1, MovieSource.POPULAR), movieOpenedEvent(3, MovieSource.SEARCH)),
            analytics.eventsNamed(AnalyticsEvents.MOVIE_OPENED),
        )
    }

    @Test
    fun `adding and removing favorites track the new state`() = runTest {
        val viewModel = createViewModel()
        val matrix = viewModel.movies.asSnapshot().first()

        viewModel.onToggleFavorite(matrix)
        viewModel.onToggleFavorite(matrix)

        assertEquals(
            listOf(
                favoriteToggledEvent(1, isFavorite = true, source = MovieSource.POPULAR),
                favoriteToggledEvent(1, isFavorite = false, source = MovieSource.POPULAR),
            ),
            analytics.events.filter { it.name.startsWith("favorite_") },
        )
    }

    @Test
    fun `ai search applies the understood filters and shows them as chips`() = runTest(mainDispatcherRule.testDispatcher) {
        val filters = MovieFilters(genres = listOf(crime, thriller), yearFrom = 1990, yearTo = 1999)
        interpreter.result = AppResult.Success(filters)
        val viewModel = createViewModel()

        viewModel.onSearchModeChange(SearchMode.AI)
        viewModel.onQueryChange("assalto com reviravolta nos anos 90")
        viewModel.onSubmitSearch()
        advanceUntilIdle()

        assertEquals(
            AiSearchUiState.Applied(
                listOf(FilterChipUi.Genre(80, "Crime"), FilterChipUi.Genre(53, "Thriller"), FilterChipUi.Years(1990, 1999)),
            ),
            viewModel.aiSearch.value,
        )
        assertEquals(listOf("Fogo contra Fogo"), viewModel.movies.asSnapshot().map { it.title })
        assertEquals(filters, moviesRepository.discovered.last())
        assertEquals(
            listOf(aiSearchEvent("assalto com reviravolta nos anos 90", AiSearchOutcome.UNDERSTOOD, filterCount = 3)),
            analytics.eventsNamed(AnalyticsEvents.AI_SEARCH),
        )
    }

    @Test
    fun `typing in ai mode does not search until submitted`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onSearchModeChange(SearchMode.AI)
        viewModel.onQueryChange("matrix")
        advanceTimeBy(MoviesViewModel.SEARCH_DEBOUNCE_MS + 1)

        assertEquals(listOf("Matrix", "Duna"), viewModel.movies.asSnapshot().map { it.title })
        assertTrue(moviesRepository.searches.isEmpty())
        assertTrue(analytics.eventsNamed(AnalyticsEvents.SEARCH).isEmpty())
    }

    @Test
    fun `not understood description falls back to the title search`() = runTest(mainDispatcherRule.testDispatcher) {
        interpreter.result = AppResult.Success(MovieFilters())
        val viewModel = createViewModel()

        viewModel.onSearchModeChange(SearchMode.AI)
        viewModel.onQueryChange("matrix")
        viewModel.onSubmitSearch()
        advanceUntilIdle()

        assertEquals(AiSearchUiState.Fallback(FallbackReason.NOT_UNDERSTOOD), viewModel.aiSearch.value)
        assertEquals(listOf("Matrix Reloaded"), viewModel.movies.asSnapshot().map { it.title })
    }

    @Test
    fun `unavailable ai falls back to the title search`() = runTest(mainDispatcherRule.testDispatcher) {
        interpreter.result = AppResult.Failure(DomainError.RateLimited)
        val viewModel = createViewModel()

        viewModel.onSearchModeChange(SearchMode.AI)
        viewModel.onQueryChange("matrix")
        viewModel.onSubmitSearch()
        advanceUntilIdle()

        assertEquals(AiSearchUiState.Fallback(FallbackReason.UNAVAILABLE), viewModel.aiSearch.value)
        assertEquals(
            listOf(aiSearchEvent("matrix", AiSearchOutcome.UNAVAILABLE, filterCount = 0)),
            analytics.eventsNamed(AnalyticsEvents.AI_SEARCH),
        )
    }

    @Test
    fun `removing chips narrows the filters and the last one goes back to popular`() = runTest(mainDispatcherRule.testDispatcher) {
        interpreter.result = AppResult.Success(MovieFilters(genres = listOf(crime), yearFrom = 1990, yearTo = 1999))
        val viewModel = createViewModel()
        viewModel.onSearchModeChange(SearchMode.AI)
        viewModel.onQueryChange("crime nos anos 90")
        viewModel.onSubmitSearch()
        advanceUntilIdle()

        viewModel.onRemoveFilter(FilterChipUi.Years(1990, 1999))
        assertEquals(AiSearchUiState.Applied(listOf(FilterChipUi.Genre(80, "Crime"))), viewModel.aiSearch.value)

        viewModel.onRemoveFilter(FilterChipUi.Genre(80, "Crime"))
        assertEquals(AiSearchUiState.Idle, viewModel.aiSearch.value)
        assertEquals(listOf("Matrix", "Duna"), viewModel.movies.asSnapshot().map { it.title })
    }

    @Test
    fun `opening a movie from ai results tracks the ai source`() = runTest(mainDispatcherRule.testDispatcher) {
        interpreter.result = AppResult.Success(MovieFilters(genres = listOf(crime)))
        val viewModel = createViewModel()
        viewModel.onSearchModeChange(SearchMode.AI)
        viewModel.onQueryChange("crime")
        viewModel.onSubmitSearch()
        advanceUntilIdle()

        viewModel.onMovieOpened(4)

        assertEquals(listOf(movieOpenedEvent(4, MovieSource.AI_SEARCH)), analytics.eventsNamed(AnalyticsEvents.MOVIE_OPENED))
    }

    @Test
    fun `switching back to title mode clears the ai search`() = runTest(mainDispatcherRule.testDispatcher) {
        interpreter.result = AppResult.Success(MovieFilters(genres = listOf(crime)))
        val viewModel = createViewModel()
        viewModel.onSearchModeChange(SearchMode.AI)
        viewModel.onQueryChange("crime")
        viewModel.onSubmitSearch()
        advanceUntilIdle()

        viewModel.onSearchModeChange(SearchMode.TITLE)

        assertEquals(AiSearchUiState.Idle, viewModel.aiSearch.value)
        assertEquals(SearchMode.TITLE, viewModel.searchMode.value)
    }

    private fun pagerOf(movies: List<Movie>): Flow<PagingData<Movie>> =
        Pager(PagingConfig(pageSize = movies.size.coerceAtLeast(1))) {
            object : PagingSource<Int, Movie>() {
                override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null

                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
                    LoadResult.Page(data = movies, prevKey = null, nextKey = null)
            }
        }.flow
}
