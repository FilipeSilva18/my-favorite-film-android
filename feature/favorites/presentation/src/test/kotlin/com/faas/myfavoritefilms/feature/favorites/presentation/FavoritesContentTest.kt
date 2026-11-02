package com.faas.myfavoritefilms.feature.favorites.presentation

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.core.ui.movie.MovieGridDefaults
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import com.faas.myfavoritefilms.core.ui.R as CoreUiR

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class FavoritesContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val favorites = listOf(
        MovieCardUi(603, "Matrix", null, null, 8.2, "8.2", isFavorite = true),
        MovieCardUi(438631, "Duna", null, null, 7.8, "7.8", isFavorite = true),
    )
    private val clicked = mutableListOf<Int>()
    private val toggled = mutableListOf<MovieCardUi>()

    private fun setContent(state: FavoritesUiState) {
        composeRule.setContent {
            MoviesTheme {
                FavoritesContent(
                    state = state,
                    onMovieClick = { clicked += it },
                    onToggleFavorite = { toggled += it },
                )
            }
        }
    }

    @Test
    fun `empty state explains how to add favorites`() {
        setContent(FavoritesUiState.Empty)

        composeRule.onNodeWithText(context.getString(R.string.favorites_empty)).assertExists()
    }

    @Test
    fun `loading shows skeleton`() {
        setContent(FavoritesUiState.Loading)

        composeRule.onNodeWithTag(MovieGridDefaults.SKELETON_TEST_TAG).assertExists()
    }

    @Test
    fun `lists favorites and click opens detail`() {
        setContent(FavoritesUiState.Content(favorites))

        composeRule.onNodeWithText("Duna").performClick()

        assertEquals(listOf(438631), clicked)
    }

    @Test
    fun `heart removes from favorites`() {
        setContent(FavoritesUiState.Content(favorites))

        composeRule
            .onAllNodesWithContentDescription(context.getString(CoreUiR.string.movie_favorite_remove))
            .onFirst()
            .performClick()

        assertEquals(listOf(favorites.first()), toggled)
    }
}
