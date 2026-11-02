package com.faas.myfavoritefilms.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.component.FavoriteIconButton
import com.faas.myfavoritefilms.core.designsystem.component.LabelChips
import com.faas.myfavoritefilms.core.designsystem.component.MoviePoster
import com.faas.myfavoritefilms.core.designsystem.component.RatingBadge
import com.faas.myfavoritefilms.core.designsystem.component.SearchField
import com.faas.myfavoritefilms.core.designsystem.component.SkeletonBox
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
class DesignSystemScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `components light`() = capture(darkTheme = false, name = "designsystem_components_light")

    @Test
    fun `components dark`() = capture(darkTheme = true, name = "designsystem_components_dark")

    private fun capture(darkTheme: Boolean, name: String) {
        composeRule.setContent {
            MoviesTheme(darkTheme = darkTheme, dynamicColor = false) { Gallery() }
        }
        composeRule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }

    @Composable
    private fun Gallery() {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SearchField(query = "matrix", onQueryChange = {}, placeholder = "Buscar", isLoading = false, clearContentDescription = null)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MoviePoster(url = null, contentDescription = "Matrix", modifier = Modifier.width(96.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    RatingBadge(label = "8.2")
                    FavoriteIconButton(isFavorite = true, onClick = {}, contentDescription = null)
                    FavoriteIconButton(isFavorite = false, onClick = {}, contentDescription = null)
                }
            }
            LabelChips(labels = listOf("Ação", "Ficção científica", "Aventura", "Drama"))
            SkeletonBox(Modifier.fillMaxWidth().height(16.dp))
        }
    }
}
