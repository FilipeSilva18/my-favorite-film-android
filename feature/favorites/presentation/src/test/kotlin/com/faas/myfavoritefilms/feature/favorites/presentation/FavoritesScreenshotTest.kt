package com.faas.myfavoritefilms.feature.favorites.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "pt-rBR-w411dp-h891dp-xxhdpi")
class FavoritesScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val content = FavoritesUiState.Content(
        List(4) { MovieCardUi(it, "Favorito ${it + 1}", null, null, 8.0, "8.0", isFavorite = true) },
    )

    @Test
    fun `list light`() = capture("favorites_list_light", content, darkTheme = false)

    @Test
    fun `list dark`() = capture("favorites_list_dark", content, darkTheme = true)

    @Test
    fun `empty`() = capture("favorites_empty", FavoritesUiState.Empty, darkTheme = false)

    private fun capture(name: String, state: FavoritesUiState, darkTheme: Boolean) {
        composeRule.setContent {
            MoviesTheme(darkTheme = darkTheme, dynamicColor = false) {
                FavoritesContent(state = state, onMovieClick = {}, onToggleFavorite = {})
            }
        }
        composeRule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }
}
