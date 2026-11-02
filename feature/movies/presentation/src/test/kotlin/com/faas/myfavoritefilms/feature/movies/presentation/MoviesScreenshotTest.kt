package com.faas.myfavoritefilms.feature.movies.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.feature.movies.presentation.model.AiSearchUiState
import com.faas.myfavoritefilms.feature.movies.presentation.model.FallbackReason
import com.faas.myfavoritefilms.feature.movies.presentation.model.FilterChipUi
import com.faas.myfavoritefilms.feature.movies.presentation.model.SearchMode
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "pt-rBR-w411dp-h891dp-xxhdpi")
class MoviesScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `list light`() = capture("movies_list_light", MoviesTestData.content(), darkTheme = false)

    @Test
    fun `list dark`() = capture("movies_list_dark", MoviesTestData.content(), darkTheme = true)

    @Test
    fun `search typing`() = capture("movies_search_query", MoviesTestData.content(), darkTheme = false, query = "filme")

    @Test
    fun `loading`() = capture("movies_loading", MoviesTestData.loading(), darkTheme = false)

    @Test
    fun `error`() = capture("movies_error", MoviesTestData.error(), darkTheme = false)

    @Test
    fun `ai filters applied`() = capture(
        "movies_ai_filters",
        MoviesTestData.content(),
        darkTheme = false,
        query = "assalto com reviravolta nos anos 90",
        searchMode = SearchMode.AI,
        aiSearch = AiSearchUiState.Applied(
            listOf(
                FilterChipUi.Genre(80, "Crime"),
                FilterChipUi.Genre(53, "Thriller"),
                FilterChipUi.Years(1990, 1999),
                FilterChipUi.MinRating(7.0),
            ),
        ),
    )

    @Test
    fun `ai fallback dark`() = capture(
        "movies_ai_fallback_dark",
        MoviesTestData.content(),
        darkTheme = true,
        query = "algo legal",
        searchMode = SearchMode.AI,
        aiSearch = AiSearchUiState.Fallback(FallbackReason.NOT_UNDERSTOOD),
    )

    @Test
    fun `search empty`() = capture("movies_search_empty", MoviesTestData.empty(), darkTheme = false, query = "xyzw")

    private fun capture(
        name: String,
        data: PagingData<MovieCardUi>,
        darkTheme: Boolean,
        query: String = "",
        searchMode: SearchMode = SearchMode.TITLE,
        aiSearch: AiSearchUiState = AiSearchUiState.Idle,
    ) {
        composeRule.setContent {
            MoviesTheme(darkTheme = darkTheme, dynamicColor = false) {
                MoviesContent(
                    query = query,
                    searchQuery = query,
                    movies = flowOf(data).collectAsLazyPagingItems(),
                    onQueryChange = {},
                    onMovieClick = {},
                    onToggleFavorite = {},
                    searchMode = searchMode,
                    aiSearch = aiSearch,
                )
            }
        }
        composeRule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }
}
