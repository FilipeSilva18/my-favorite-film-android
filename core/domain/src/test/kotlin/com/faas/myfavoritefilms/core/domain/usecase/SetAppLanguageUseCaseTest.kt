package com.faas.myfavoritefilms.core.domain.usecase

import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.testing.language.FakeAppLanguageApplier
import com.faas.myfavoritefilms.core.testing.repository.FakeUserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SetAppLanguageUseCaseTest {

    @Test
    fun `saves the language and applies it to the app`() = runTest {
        val repository = FakeUserPreferencesRepository()
        val applier = FakeAppLanguageApplier()

        SetAppLanguageUseCase(repository, applier)(AppLanguage.ENGLISH)

        assertEquals(AppLanguage.ENGLISH, repository.current.language)
        assertEquals(listOf(AppLanguage.ENGLISH), applier.applied)
    }
}
