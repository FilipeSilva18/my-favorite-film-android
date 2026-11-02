package com.faas.myfavoritefilms.core.domain.usecase

import app.cash.turbine.test
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import com.faas.myfavoritefilms.core.testing.repository.FakeUserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveUserPreferencesUseCaseTest {

    private val repository = FakeUserPreferencesRepository()

    @Test
    fun `emits defaults and every change`() = runTest {
        ObserveUserPreferencesUseCase(repository)().test {
            assertEquals(UserPreferences(), awaitItem())
            repository.setThemeMode(ThemeMode.DARK)
            assertEquals(ThemeMode.DARK, awaitItem().themeMode)
        }
    }
}
