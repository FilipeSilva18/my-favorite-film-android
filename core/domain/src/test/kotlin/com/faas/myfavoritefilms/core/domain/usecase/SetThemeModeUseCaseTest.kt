package com.faas.myfavoritefilms.core.domain.usecase

import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.testing.repository.FakeUserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SetThemeModeUseCaseTest {

    @Test
    fun `saves the theme mode`() = runTest {
        val repository = FakeUserPreferencesRepository()

        SetThemeModeUseCase(repository)(ThemeMode.LIGHT)

        assertEquals(ThemeMode.LIGHT, repository.current.themeMode)
    }
}
