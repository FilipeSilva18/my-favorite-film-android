package com.faas.myfavoritefilms.feature.favorites.domain.usecase

import app.cash.turbine.test
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.domain.FakeFavoritesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveFavoritesUseCaseTest {

    private val repository = FakeFavoritesRepository()
    private val observe = ObserveFavoritesUseCase(repository)

    @Test
    fun `emits the full favorites list`() = runTest {
        val matrix = FavoriteMovie(603, "Matrix", null, 8.2)
        observe().test {
            assertEquals(emptyList<FavoriteMovie>(), awaitItem())
            repository.add(matrix)
            assertEquals(listOf(matrix), awaitItem())
        }
    }
}
