package com.faas.myfavoritefilms.feature.settings.presentation

import android.content.Context
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class SettingsContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val themes = mutableListOf<ThemeMode>()
    private val dynamicColors = mutableListOf<Boolean>()
    private val languages = mutableListOf<AppLanguage>()

    private fun setContent(preferences: UserPreferences, supportsDynamicColor: Boolean = true) {
        composeRule.setContent {
            MoviesTheme {
                SettingsContent(
                    state = SettingsUiState.Success(preferences),
                    supportsDynamicColor = supportsDynamicColor,
                    onThemeModeSelected = { themes += it },
                    onDynamicColorChanged = { dynamicColors += it },
                    onLanguageSelected = { languages += it },
                )
            }
        }
    }

    @Test
    fun `current choices are selected and clicks send the new values`() {
        setContent(UserPreferences(themeMode = ThemeMode.DARK, useDynamicColor = true, language = AppLanguage.ENGLISH))

        composeRule.onNodeWithText(context.getString(R.string.settings_theme_dark)).assertIsSelected()
        composeRule.onNodeWithText(context.getString(R.string.settings_language_english)).assertIsSelected()
        composeRule.onNodeWithText(context.getString(R.string.settings_dynamic_color)).assertIsOn()

        composeRule.onNodeWithText(context.getString(R.string.settings_theme_light)).performClick()
        composeRule.onNodeWithText(context.getString(R.string.settings_dynamic_color)).performClick()
        composeRule.onNodeWithText(context.getString(R.string.settings_language_portuguese)).performScrollTo().performClick()

        assertEquals(listOf(ThemeMode.LIGHT), themes)
        assertEquals(listOf(false), dynamicColors)
        assertEquals(listOf(AppLanguage.PORTUGUESE_BRAZIL), languages)
    }

    @Test
    fun `dynamic color option is hidden when not supported`() {
        setContent(UserPreferences(), supportsDynamicColor = false)

        composeRule.onNodeWithText(context.getString(R.string.settings_dynamic_color)).assertDoesNotExist()
    }
}
