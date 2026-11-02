package com.faas.myfavoritefilms.core.domain.usecase

import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import com.faas.myfavoritefilms.core.testing.language.FakeAppLanguageApplier
import com.faas.myfavoritefilms.core.testing.repository.FakeUserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SyncAppLanguageUseCaseTest {

    @Test
    fun `stored language follows the language active in the system`() = runTest {
        val repository = FakeUserPreferencesRepository(UserPreferences(language = AppLanguage.ENGLISH))
        val applier = FakeAppLanguageApplier(systemLanguage = AppLanguage.PORTUGUESE_BRAZIL)

        SyncAppLanguageUseCase(repository, applier)()

        assertEquals(AppLanguage.PORTUGUESE_BRAZIL, repository.current.language)
        assertTrue(applier.applied.isEmpty())
    }
}
