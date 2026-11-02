package com.faas.myfavoritefilms.core.domain.usecase

import com.faas.myfavoritefilms.core.testing.repository.FakeUserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SetDynamicColorUseCaseTest {

    @Test
    fun `saves the dynamic color flag`() = runTest {
        val repository = FakeUserPreferencesRepository()

        SetDynamicColorUseCase(repository)(false)

        assertEquals(false, repository.current.useDynamicColor)
    }
}
