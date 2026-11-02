package com.faas.myfavoritefilms.feature.moviedetail.presentation

import android.content.Context
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.feature.moviedetail.presentation.component.MOVIE_DETAIL_SKELETON_TEST_TAG
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import com.faas.myfavoritefilms.core.ui.R as CoreUiR

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class MovieDetailContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private var backs = 0
    private var retries = 0
    private var toggles = 0

    private fun setContent(state: MovieDetailUiState) {
        composeRule.setContent {
            MoviesTheme {
                MovieDetailContent(
                    state = state,
                    onBack = { backs++ },
                    onRetry = { retries++ },
                    onToggleFavorite = { toggles++ },
                )
            }
        }
    }

    @Test
    fun `success shows movie data and server driven blocks`() {
        setContent(MovieDetailUiState.Success(MovieDetailTestData.movie, isFavorite = false))

        composeRule.onAllNodesWithText("Matrix").onFirst().assertExists()
        composeRule.onNodeWithText("Bem-vindo ao mundo real.").assertExists()
        composeRule.onNodeWithText("1999 · 2h 16min").assertExists()

        val list = composeRule.onNode(hasScrollAction())
        list.performScrollToNode(hasText("Orçamento"))
        composeRule.onNodeWithText("$63,000,000").assertExists()
        list.performScrollToNode(hasText("Ver no IMDb"))
        composeRule.onNodeWithText("Ver no IMDb").assertExists()
    }

    @Test
    fun `favorite and back invoke callbacks`() {
        setContent(MovieDetailUiState.Success(MovieDetailTestData.movie, isFavorite = false))

        composeRule.onNodeWithContentDescription(context.getString(CoreUiR.string.movie_favorite_add)).performClick()
        composeRule.onNodeWithContentDescription(context.getString(CoreUiR.string.action_back)).performClick()

        assertEquals(1, toggles)
        assertEquals(1, backs)
    }

    @Test
    fun `loading shows skeleton without favorite button`() {
        setContent(MovieDetailUiState.Loading)

        composeRule.onNodeWithTag(MOVIE_DETAIL_SKELETON_TEST_TAG).assertExists()
        composeRule.onNodeWithContentDescription(context.getString(CoreUiR.string.movie_favorite_add)).assertDoesNotExist()
    }

    @Test
    fun `error allows retry`() {
        setContent(MovieDetailUiState.Error(DomainError.NotFound))

        composeRule.onNodeWithText(context.getString(CoreUiR.string.error_not_found)).assertExists()
        composeRule.onNodeWithText(context.getString(CoreUiR.string.action_retry)).performClick()

        assertEquals(1, retries)
    }
}
