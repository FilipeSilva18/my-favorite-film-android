package com.faas.myfavoritefilms.feature.movies.data.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.TestPager
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.common.DomainException
import com.faas.myfavoritefilms.core.testing.analytics.TestErrorReporter
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.MovieDto
import com.faas.myfavoritefilms.feature.movies.data.remote.dto.PagedResponseDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class MoviesPagingSourceTest {

    private val config = PagingConfig(pageSize = 20, initialLoadSize = 20, enablePlaceholders = false)
    private val errorReporter = TestErrorReporter()

    private fun response(page: Int, totalPages: Int) = PagedResponseDto(
        page = page,
        results = listOf(MovieDto(id = page * 100, title = "Filme $page")),
        totalPages = totalPages,
        totalResults = totalPages * 20,
    )

    @Test
    fun `first page has no prevKey and points to page 2`() = runTest {
        val pager = TestPager(config, MoviesPagingSource(errorReporter, ORIGIN) { page -> response(page, totalPages = 3) })

        val result = pager.refresh() as LoadResult.Page

        assertEquals(listOf(100), result.data.map { it.id })
        assertNull(result.prevKey)
        assertEquals(2, result.nextKey)
    }

    @Test
    fun `last page ends pagination`() = runTest {
        val pager = TestPager(config, MoviesPagingSource(errorReporter, ORIGIN) { page -> response(page, totalPages = 2) })

        pager.refresh()
        val last = pager.append() as LoadResult.Page

        assertEquals(listOf(200), last.data.map { it.id })
        assertNull(last.nextKey)
    }

    @Test
    fun `respects the TMDB 500 page limit`() = runTest {
        val pager = TestPager(config, MoviesPagingSource(errorReporter, ORIGIN) { page -> response(page, totalPages = 40_000) })

        val result = pager.refresh(initialKey = 500) as LoadResult.Page

        assertNull(result.nextKey)
    }

    @Test
    fun `network failure becomes DomainError Network`() = runTest {
        val pager = TestPager(config, MoviesPagingSource(errorReporter, ORIGIN) { throw IOException("offline") })

        val result = pager.refresh()

        assertTrue(result is LoadResult.Error)
        val error = (result as LoadResult.Error).throwable as DomainException
        assertEquals(DomainError.Network, error.error)
        assertTrue("missing connectivity is expected, not a non fatal", errorReporter.errors.isEmpty())
    }

    @Test
    fun `unexpected error is reported as non fatal with its origin`() = runTest {
        val pager = TestPager(config, MoviesPagingSource(errorReporter, ORIGIN) { throw IllegalStateException("json") })

        pager.refresh()

        assertEquals(listOf(ORIGIN), errorReporter.errors.map { it.second["origin"] })
    }

    private companion object {
        const val ORIGIN = "movies_test"
    }
}
