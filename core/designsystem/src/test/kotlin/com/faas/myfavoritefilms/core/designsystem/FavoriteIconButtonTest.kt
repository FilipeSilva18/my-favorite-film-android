package com.faas.myfavoritefilms.core.designsystem

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.component.FavoriteIconButton
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class FavoriteIconButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `click invokes callback and uses the given description`() {
        var clicks = 0
        composeRule.setContent {
            MoviesTheme {
                FavoriteIconButton(isFavorite = false, onClick = { clicks++ }, contentDescription = "Favoritar")
            }
        }

        composeRule.onNodeWithContentDescription("Favoritar").performClick()

        assertEquals(1, clicks)
    }
}
