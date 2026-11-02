package com.faas.myfavoritefilms.feature.moviedetail.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "pt-rBR-w411dp-h891dp-xxhdpi")
class MovieDetailScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `success light`() =
        capture("movie_detail_success_light", MovieDetailUiState.Success(MovieDetailTestData.movie, true), false)

    @Test
    fun `success dark`() =
        capture("movie_detail_success_dark", MovieDetailUiState.Success(MovieDetailTestData.movie, true), true)

    @Test
    fun `loading`() = capture("movie_detail_loading", MovieDetailUiState.Loading, false)

    @Test
    fun `error`() = capture("movie_detail_error", MovieDetailUiState.Error(DomainError.Network), false)

    private fun capture(name: String, state: MovieDetailUiState, darkTheme: Boolean) {
        composeRule.setContent {
            MoviesTheme(darkTheme = darkTheme, dynamicColor = false) {
                MovieDetailContent(state = state, onBack = {}, onRetry = {}, onToggleFavorite = {})
            }
        }
        composeRule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }
}
