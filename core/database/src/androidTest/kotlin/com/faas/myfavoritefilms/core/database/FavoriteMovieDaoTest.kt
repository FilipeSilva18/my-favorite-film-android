package com.faas.myfavoritefilms.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.faas.myfavoritefilms.core.database.dao.FavoriteMovieDao
import com.faas.myfavoritefilms.core.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteMovieDaoTest {
    private lateinit var database: MoviesDatabase
    private lateinit var dao: FavoriteMovieDao

    private fun entity(id: Int, savedAt: Long) =
        FavoriteMovieEntity(id = id, title = "Filme $id", posterUrl = null, rating = 7.0, savedAt = savedAt)

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDatabase::class.java,
        ).build()
        dao = database.favoriteMovieDao()
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun upsertInsertsAndUpdatesWithoutDuplicating() = runTest {
        dao.upsert(entity(1, savedAt = 10))
        dao.upsert(entity(1, savedAt = 20).copy(title = "Atualizado"))

        dao.observeAll().test {
            assertEquals(listOf(entity(1, 20).copy(title = "Atualizado")), awaitItem())
        }
    }

    @Test
    fun observeAllOrdersFromNewestToOldest() = runTest {
        dao.upsert(entity(1, savedAt = 10))
        dao.upsert(entity(2, savedAt = 30))
        dao.upsert(entity(3, savedAt = 20))

        dao.observeAll().test {
            assertEquals(listOf(2, 3, 1), awaitItem().map { it.id })
        }
    }

    @Test
    fun existsAndDeleteById() = runTest {
        dao.upsert(entity(1, savedAt = 10))
        assertTrue(dao.exists(1))

        dao.deleteById(1)
        assertFalse(dao.exists(1))
    }

    @Test
    fun flowsReactToWrites() = runTest {
        dao.observeExists(7).test {
            assertFalse(awaitItem())
            dao.upsert(entity(7, savedAt = 1))
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        dao.observeIds().test {
            assertEquals(listOf(7), awaitItem())
        }
    }
}
