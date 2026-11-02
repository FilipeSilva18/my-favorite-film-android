package com.faas.myfavoritefilms.feature.settings.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "pt-rBR-w411dp-h891dp-xxhdpi")
class SettingsScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `settings light`() = capture("settings_light", darkTheme = false)

    @Test
    fun `settings dark`() = capture("settings_dark", darkTheme = true)

    private fun capture(name: String, darkTheme: Boolean) {
        composeRule.setContent {
            MoviesTheme(darkTheme = darkTheme, dynamicColor = false) {
                SettingsContent(
                    state = SettingsUiState.Success(UserPreferences(themeMode = ThemeMode.SYSTEM)),
                    supportsDynamicColor = true,
                    onThemeModeSelected = {},
                    onDynamicColorChanged = {},
                    onLanguageSelected = {},
                )
            }
        }
        composeRule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }
}
