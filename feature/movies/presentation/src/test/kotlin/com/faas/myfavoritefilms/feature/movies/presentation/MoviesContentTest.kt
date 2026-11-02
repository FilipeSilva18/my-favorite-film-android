package com.faas.myfavoritefilms.feature.movies.presentation

import android.content.Context
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.core.ui.movie.MovieGridDefaults
import com.faas.myfavoritefilms.feature.movies.presentation.model.AiSearchUiState
import com.faas.myfavoritefilms.feature.movies.presentation.model.FallbackReason
import com.faas.myfavoritefilms.feature.movies.presentation.model.FilterChipUi
import com.faas.myfavoritefilms.feature.movies.presentation.model.SearchMode
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import com.faas.myfavoritefilms.core.ui.R as CoreUiR

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class MoviesContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val queries = mutableListOf<String>()
    private val clickedIds = mutableListOf<Int>()
    private val toggled = mutableListOf<MovieCardUi>()
    private val modes = mutableListOf<SearchMode>()
    private val removedChips = mutableListOf<FilterChipUi>()
    private var submits = 0

    private fun setContent(
        data: PagingData<MovieCardUi>,
        query: String = "",
        searchQuery: String = query,
        searchMode: SearchMode = SearchMode.TITLE,
        aiSearch: AiSearchUiState = AiSearchUiState.Idle,
    ) {
        composeRule.setContent {
            MoviesTheme {
                MoviesContent(
                    query = query,
                    searchQuery = searchQuery,
                    movies = flowOf(data).collectAsLazyPagingItems(),
                    onQueryChange = { queries += it },
                    onMovieClick = { clickedIds += it },
                    onToggleFavorite = { toggled += it },
                    searchMode = searchMode,
                    aiSearch = aiSearch,
                    onSearchModeChange = { modes += it },
                    onSubmitSearch = { submits++ },
                    onRemoveFilter = { removedChips += it },
                )
            }
        }
    }

    @Test
    fun `content shows movies and click opens detail`() {
        setContent(MoviesTestData.content())

        composeRule.onNodeWithText("Filme 1").assertExists()
        composeRule.onNodeWithText("Filme 1").performClick()

        assertEquals(listOf(1), clickedIds)
    }

    @Test
    fun `heart favorites without opening detail`() {
        setContent(MoviesTestData.content())

        composeRule
            .onAllNodesWithContentDescription(context.getString(CoreUiR.string.movie_favorite_add))
            .onFirst()
            .performClick()

        assertEquals(1, toggled.size)
        assertEquals(emptyList<Int>(), clickedIds)
    }

    @Test
    fun `initial load shows skeleton`() {
        setContent(MoviesTestData.loading())

        composeRule.onNodeWithTag(MovieGridDefaults.SKELETON_TEST_TAG).assertExists()
    }

    @Test
    fun `error shows message and retry`() {
        setContent(MoviesTestData.error())

        composeRule.onNodeWithText(context.getString(CoreUiR.string.error_network)).assertExists()
        composeRule.onNodeWithText(context.getString(CoreUiR.string.action_retry)).assertExists()
    }

    @Test
    fun `search without results shows the searched term`() {
        setContent(MoviesTestData.empty(), query = "xyzw")

        composeRule.onNodeWithText(context.getString(R.string.movies_search_empty, "xyzw")).assertExists()
    }

    @Test
    fun `typing in search sends the text`() {
        setContent(MoviesTestData.content())

        composeRule.onNode(hasSetTextAction()).performTextInput("duna")

        assertEquals("duna", queries.last())
    }

    @Test
    fun `choosing the ai mode sends the new mode`() {
        setContent(MoviesTestData.content())

        composeRule.onNodeWithText(context.getString(R.string.movies_search_mode_ai)).performClick()

        assertEquals(listOf(SearchMode.AI), modes)
    }

    @Test
    fun `ai mode shows the describe placeholder and the hint`() {
        setContent(MoviesTestData.content(), searchMode = SearchMode.AI)

        composeRule.onNodeWithText(context.getString(R.string.movies_ai_placeholder)).assertExists()
        composeRule.onNodeWithText(context.getString(R.string.movies_ai_hint)).assertExists()
    }

    @Test
    fun `keyboard search submits the ai search`() {
        setContent(MoviesTestData.content(), query = "suspense anos 90", searchMode = SearchMode.AI)

        composeRule.onNode(hasSetTextAction()).performImeAction()

        assertEquals(1, submits)
    }

    @Test
    fun `applied filters show as chips and tapping one removes it`() {
        val years = FilterChipUi.Years(1990, 1999)
        setContent(
            MoviesTestData.content(),
            searchMode = SearchMode.AI,
            aiSearch = AiSearchUiState.Applied(listOf(FilterChipUi.Genre(80, "Crime"), years)),
        )

        composeRule.onNodeWithText("Crime").assertExists()
        composeRule.onNodeWithText(context.getString(R.string.movies_filter_years_range, 1990, 1999)).performClick()

        assertEquals(listOf<FilterChipUi>(years), removedChips)
    }

    @Test
    fun `fallback explains that the title search is shown`() {
        setContent(
            MoviesTestData.content(),
            searchMode = SearchMode.AI,
            aiSearch = AiSearchUiState.Fallback(FallbackReason.UNAVAILABLE),
        )

        composeRule.onNodeWithText(context.getString(R.string.movies_ai_unavailable)).assertExists()
    }

    @Test
    fun `filters without results show the filters empty message`() {
        setContent(
            MoviesTestData.empty(),
            searchMode = SearchMode.AI,
            aiSearch = AiSearchUiState.Applied(listOf(FilterChipUi.Genre(80, "Crime"))),
        )

        composeRule.onNodeWithText(context.getString(R.string.movies_ai_filters_empty)).assertExists()
    }
}
