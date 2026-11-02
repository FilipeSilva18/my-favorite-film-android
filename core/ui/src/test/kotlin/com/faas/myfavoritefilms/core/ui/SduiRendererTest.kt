package com.faas.myfavoritefilms.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.common.sdui.KeyValueItem
import com.faas.myfavoritefilms.core.common.sdui.PersonItem
import com.faas.myfavoritefilms.core.common.sdui.SduiComponent
import com.faas.myfavoritefilms.core.common.sdui.VideoItem
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.ui.sdui.SduiComponentView
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class SduiRendererTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val openedUris = mutableListOf<String>()
    private val fakeUriHandler = object : UriHandler {
        override fun openUri(uri: String) {
            openedUris += uri
        }
    }

    private val components = listOf(
        SduiComponent.SectionTitle("Sobre"),
        SduiComponent.KeyValueList(
            title = "Ficha técnica",
            items = listOf(
                KeyValueItem("Orçamento", "$63,000,000", icon = "money"),
                KeyValueItem("Chave nova", "valor", icon = "icone_que_nao_existe"),
            ),
        ),
        SduiComponent.ChipGroup("Produtoras", listOf("Warner Bros.")),
        SduiComponent.TextBlock(title = null, text = "Texto livre"),
        SduiComponent.Divider,
        SduiComponent.LinkButton("Ver no IMDb", "https://www.imdb.com/title/tt0133093"),
        SduiComponent.PersonCarousel("Elenco", listOf(PersonItem("Keanu Reeves", "Neo"))),
        SduiComponent.VideoCarousel("Trailers", listOf(VideoItem("Trailer oficial", "https://www.youtube.com/watch?v=abc"))),
    )

    @Test
    fun `renders every block type`() {
        composeRule.setContent {
            MoviesTheme { Column { components.forEach { SduiComponentView(it) } } }
        }

        listOf("Sobre", "Ficha técnica", "Orçamento", "$63,000,000", "Chave nova", "Produtoras", "Warner Bros.", "Texto livre", "Ver no IMDb", "Elenco", "Keanu Reeves", "Neo", "Trailers", "Trailer oficial")
            .forEach { composeRule.onNodeWithText(it).assertExists() }
    }

    @Test
    fun `link opens the server provided url`() {
        composeRule.setContent {
            CompositionLocalProvider(LocalUriHandler provides fakeUriHandler) {
                MoviesTheme { Column { components.forEach { SduiComponentView(it) } } }
            }
        }

        composeRule.onNodeWithText("Ver no IMDb").performClick()

        assertEquals(listOf("https://www.imdb.com/title/tt0133093"), openedUris)
    }

    @Test
    fun `video card opens the video url`() {
        val videos = components.filterIsInstance<SduiComponent.VideoCarousel>().single()
        composeRule.setContent {
            CompositionLocalProvider(LocalUriHandler provides fakeUriHandler) {
                MoviesTheme { SduiComponentView(videos) }
            }
        }

        composeRule.onNodeWithText("Trailer oficial").performClick()

        assertEquals(listOf("https://www.youtube.com/watch?v=abc"), openedUris)
    }
}
