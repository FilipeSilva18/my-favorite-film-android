package com.faas.myfavoritefilms.presentation

import app.cash.turbine.test
import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import com.faas.myfavoritefilms.core.domain.usecase.ObserveUserPreferencesUseCase
import com.faas.myfavoritefilms.core.domain.usecase.SyncAppLanguageUseCase
import com.faas.myfavoritefilms.core.testing.MainDispatcherRule
import com.faas.myfavoritefilms.core.testing.language.FakeAppLanguageApplier
import com.faas.myfavoritefilms.core.testing.repository.FakeUserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainActivityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeUserPreferencesRepository()
    private val applier = FakeAppLanguageApplier(systemLanguage = AppLanguage.ENGLISH)

    private fun createViewModel() = MainActivityViewModel(
        observeUserPreferences = ObserveUserPreferencesUseCase(repository),
        syncAppLanguage = SyncAppLanguageUseCase(repository, applier),
    )

    @Test
    fun `exposes preferences and reacts to theme changes`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val initial = expectMostRecentItem() as MainActivityUiState.Success
            assertTrue(initial.useDarkTheme(isSystemInDarkTheme = true))

            repository.setThemeMode(ThemeMode.LIGHT)
            assertFalse((expectMostRecentItem() as MainActivityUiState.Success).useDarkTheme(isSystemInDarkTheme = true))

            repository.setThemeMode(ThemeMode.DARK)
            assertTrue((expectMostRecentItem() as MainActivityUiState.Success).useDarkTheme(isSystemInDarkTheme = false))
        }
    }

    @Test
    fun `syncs the stored language with the language active in the system on start`() {
        createViewModel()

        assertEquals(AppLanguage.ENGLISH, repository.current.language)
    }

    @Test
    fun `system theme mode follows the device`() {
        val state = MainActivityUiState.Success(UserPreferences(themeMode = ThemeMode.SYSTEM))

        assertTrue(state.useDarkTheme(isSystemInDarkTheme = true))
        assertFalse(state.useDarkTheme(isSystemInDarkTheme = false))
    }
}
