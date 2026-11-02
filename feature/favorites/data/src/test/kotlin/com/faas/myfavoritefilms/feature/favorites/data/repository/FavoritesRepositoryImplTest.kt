package com.faas.myfavoritefilms.feature.favorites.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.faas.myfavoritefilms.core.database.MoviesDatabase
import com.faas.myfavoritefilms.feature.favorites.api.FavoriteMovie
import com.faas.myfavoritefilms.feature.favorites.data.datasource.FavoritesLocalDataSource
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class FavoritesRepositoryImplTest {
    private lateinit var database: MoviesDatabase
    private lateinit var repository: FavoritesRepositoryImpl

    private val matrix = FavoriteMovie(603, "Matrix", "https://img/matrix.jpg", 8.2)
    private val dune = FavoriteMovie(438631, "Duna", null, 7.8)

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDatabase::class.java,
        ).allowMainThreadQueries().build()
        repository = FavoritesRepositoryImpl(FavoritesLocalDataSource(database.favoriteMovieDao()))
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun `add and remove are reflected by isFavorite`() = runTest {
        assertFalse(repository.isFavorite(603))
        repository.add(matrix)
        assertTrue(repository.isFavorite(603))
        repository.remove(603)
        assertFalse(repository.isFavorite(603))
    }

    @Test
    fun `observeFavorites returns domain models newest first`() = runTest {
        repository.add(matrix)
        Thread.sleep(5)
        repository.add(dune)

        repository.observeFavorites().test {
            assertEquals(listOf(dune, matrix), awaitItem())
        }
    }

    @Test
    fun `observeFavoriteIds and observeIsFavorite emit on every change`() = runTest {
        repository.observeFavoriteIds().test {
            assertEquals(emptySet<Int>(), awaitItem())
            repository.add(matrix)
            assertEquals(setOf(603), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        repository.observeIsFavorite(603).test {
            assertTrue(awaitItem())
            repository.remove(603)
            assertFalse(awaitItem())
        }
    }

    @Test
    fun `adding the same movie again does not duplicate (upsert)`() = runTest {
        repository.add(matrix)
        repository.add(matrix.copy(rating = 9.0))

        repository.observeFavorites().test {
            assertEquals(listOf(matrix.copy(rating = 9.0)), awaitItem())
        }
    }
}
