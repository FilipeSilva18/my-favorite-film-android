package com.faas.myfavoritefilms.feature.favorites.domain.usecase

import app.cash.turbine.test
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.domain.FakeFavoritesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveFavoriteIdsUseCaseImplTest {

    private val repository = FakeFavoritesRepository()
    private val observe = ObserveFavoriteIdsUseCaseImpl(repository)

    @Test
    fun `emits the id set on every change`() = runTest {
        observe().test {
            assertEquals(emptySet<Int>(), awaitItem())
            repository.add(FavoriteMovie(1, "A", null, 1.0))
            assertEquals(setOf(1), awaitItem())
            repository.add(FavoriteMovie(2, "B", null, 2.0))
            assertEquals(setOf(1, 2), awaitItem())
            repository.remove(1)
            assertEquals(setOf(2), awaitItem())
        }
    }
}
