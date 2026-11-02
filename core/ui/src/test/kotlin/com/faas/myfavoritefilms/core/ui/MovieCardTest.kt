package com.faas.myfavoritefilms.core.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.ui.movie.MovieCard
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class MovieCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val movie = MovieCardUi(603, "Matrix", null, "1999", 8.2, "8.2", isFavorite = false)

    @Test
    fun `shows year and rating and separates card click from heart click`() {
        var cardClicks = 0
        var favoriteClicks = 0
        composeRule.setContent {
            MoviesTheme {
                MovieCard(movie = movie, onClick = { cardClicks++ }, onToggleFavorite = { favoriteClicks++ })
            }
        }

        composeRule.onNodeWithText("1999").assertExists()
        composeRule.onNodeWithText("8.2").assertExists()

        composeRule.onNodeWithContentDescription(context.getString(R.string.movie_favorite_add)).performClick()
        assertEquals(1, favoriteClicks)
        assertEquals(0, cardClicks)

        composeRule.onNodeWithText("1999").performClick()
        assertEquals(1, cardClicks)
    }

    @Test
    fun `heart description changes when already favorite`() {
        composeRule.setContent {
            MoviesTheme { MovieCard(movie = movie.copy(isFavorite = true), onClick = {}, onToggleFavorite = {}) }
        }

        composeRule.onNodeWithContentDescription(context.getString(R.string.movie_favorite_remove)).assertExists()
    }
}
