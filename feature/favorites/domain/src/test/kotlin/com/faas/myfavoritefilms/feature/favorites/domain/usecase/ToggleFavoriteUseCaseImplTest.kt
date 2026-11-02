package com.faas.myfavoritefilms.feature.favorites.domain.usecase

import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.domain.FakeFavoritesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ToggleFavoriteUseCaseImplTest {

    private val repository = FakeFavoritesRepository()
    private val toggle = ToggleFavoriteUseCaseImpl(repository)
    private val movie = FavoriteMovie(id = 1, title = "Matrix", posterUrl = null, rating = 8.2)

    @Test
    fun `toggles between favorite and not favorite`() = runTest {
        assertTrue(toggle(movie))
        assertTrue(repository.isFavorite(1))

        assertFalse(toggle(movie))
        assertFalse(repository.isFavorite(1))
    }
}
