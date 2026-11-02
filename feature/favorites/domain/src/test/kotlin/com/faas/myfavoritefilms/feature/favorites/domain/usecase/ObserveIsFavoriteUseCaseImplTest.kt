package com.faas.myfavoritefilms.feature.favorites.domain.usecase

import app.cash.turbine.test
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.domain.FakeFavoritesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ObserveIsFavoriteUseCaseImplTest {

    private val repository = FakeFavoritesRepository()
    private val observe = ObserveIsFavoriteUseCaseImpl(repository)

    @Test
    fun `reflects adding and removing the observed movie`() = runTest {
        observe(603).test {
            assertFalse(awaitItem())
            repository.add(FavoriteMovie(603, "Matrix", null, 8.2))
            assertTrue(awaitItem())
            repository.remove(603)
            assertFalse(awaitItem())
        }
    }
}
