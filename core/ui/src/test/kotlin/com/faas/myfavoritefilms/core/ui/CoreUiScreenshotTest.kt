package com.faas.myfavoritefilms.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.common.sdui.KeyValueItem
import com.faas.myfavoritefilms.core.common.sdui.PersonItem
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.core.common.sdui.VideoItem
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.ui.movie.MovieCard
import com.faas.myfavoritefilms.core.ui.movie.MovieCardSkeleton
import com.faas.myfavoritefilms.core.ui.movie.MovieCardUi
import com.faas.myfavoritefilms.core.ui.sdui.SduiComponentView
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "pt-rBR-w411dp-h891dp-xxhdpi")
class CoreUiScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `movie cards light`() = capture("core_ui_movie_cards_light", darkTheme = false) { Cards() }

    @Test
    fun `movie cards dark`() = capture("core_ui_movie_cards_dark", darkTheme = true) { Cards() }

    @Test
    fun `sdui light`() = capture("core_ui_sdui_light", darkTheme = false) { Sdui() }

    @Test
    fun `sdui dark`() = capture("core_ui_sdui_dark", darkTheme = true) { Sdui() }

    @Test
    fun `error`() = capture("core_ui_error_light", darkTheme = false) {
        ErrorContent(error = DomainError.Network, onRetry = {})
    }

    private fun capture(name: String, darkTheme: Boolean, content: @Composable () -> Unit) {
        composeRule.setContent {
            MoviesTheme(darkTheme = darkTheme, dynamicColor = false) {
                Column(Modifier.background(MaterialTheme.colorScheme.background).padding(16.dp)) { content() }
            }
        }
        composeRule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }

    @Composable
    private fun Cards() {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MovieCard(
                movie = MovieCardUi(603, "Matrix", null, "1999", 8.2, "8.2", isFavorite = true),
                onClick = {},
                onToggleFavorite = {},
                modifier = Modifier.width(160.dp),
            )
            MovieCardSkeleton(Modifier.width(160.dp))
        }
    }

    @Composable
    private fun Sdui() {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf(
                SduiComponent.KeyValueList(
                    title = "Ficha técnica",
                    items = listOf(
                        KeyValueItem("Título original", "The Matrix", "info"),
                        KeyValueItem("Lançamento", "30/03/1999", "calendar"),
                        KeyValueItem("Orçamento", "$63,000,000", "money"),
                    ),
                ),
                SduiComponent.ChipGroup("Produtoras", listOf("Warner Bros.", "Village Roadshow")),
                SduiComponent.PersonCarousel("Elenco", listOf(PersonItem("Keanu Reeves", "Neo"), PersonItem("Carrie-Anne Moss", "Trinity"))),
                SduiComponent.VideoCarousel("Trailers", listOf(VideoItem("Trailer oficial", "https://www.youtube.com/watch?v=abc"))),
                SduiComponent.LinkButton("Ver no IMDb", "https://www.imdb.com"),
            ).forEach { SduiComponentView(it) }
        }
    }
}
