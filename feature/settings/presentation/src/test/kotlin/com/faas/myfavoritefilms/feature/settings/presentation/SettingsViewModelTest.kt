package com.faas.myfavoritefilms.feature.settings.presentation

import app.cash.turbine.test
import com.faas.myfavoritefilms.core.analytics.settingsChangedEvent
import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import com.faas.myfavoritefilms.core.domain.usecase.ObserveUserPreferencesUseCase
import com.faas.myfavoritefilms.core.domain.usecase.SetAppLanguageUseCase
import com.faas.myfavoritefilms.core.domain.usecase.SetDynamicColorUseCase
import com.faas.myfavoritefilms.core.domain.usecase.SetThemeModeUseCase
import com.faas.myfavoritefilms.core.testing.MainDispatcherRule
import com.faas.myfavoritefilms.core.testing.analytics.TestAnalytics
import com.faas.myfavoritefilms.core.testing.language.FakeAppLanguageApplier
import com.faas.myfavoritefilms.core.testing.repository.FakeUserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeUserPreferencesRepository()
    private val applier = FakeAppLanguageApplier()
    private val analytics = TestAnalytics()

    private fun createViewModel() = SettingsViewModel(
        observeUserPreferences = ObserveUserPreferencesUseCase(repository),
        setThemeMode = SetThemeModeUseCase(repository),
        setDynamicColor = SetDynamicColorUseCase(repository),
        setAppLanguage = SetAppLanguageUseCase(repository, applier),
        analytics = analytics,
    )

    @Test
    fun `shows the stored preferences`() = runTest {
        createViewModel().uiState.test {
            assertEquals(SettingsUiState.Success(UserPreferences()), expectMostRecentItem())
        }
    }

    @Test
    fun `changing theme and dynamic color persists and tracks`() = runTest {
        val viewModel = createViewModel()

        viewModel.onThemeModeSelected(ThemeMode.DARK)
        viewModel.onDynamicColorChanged(false)

        assertEquals(ThemeMode.DARK, repository.current.themeMode)
        assertEquals(false, repository.current.useDynamicColor)
        assertEquals(
            listOf(settingsChangedEvent("theme_mode", "dark"), settingsChangedEvent("dynamic_color", "false")),
            analytics.events,
        )
    }

    @Test
    fun `changing language persists, applies it to the app and tracks`() = runTest {
        val viewModel = createViewModel()

        viewModel.onLanguageSelected(AppLanguage.ENGLISH)
        viewModel.onLanguageSelected(AppLanguage.SYSTEM)

        assertEquals(AppLanguage.SYSTEM, repository.current.language)
        assertEquals(listOf(AppLanguage.ENGLISH, AppLanguage.SYSTEM), applier.applied)
        assertEquals(
            listOf(settingsChangedEvent("language", "en"), settingsChangedEvent("language", "system")),
            analytics.events,
        )
    }
}
