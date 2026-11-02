package com.faas.myfavoritefilms.core.analytics

import com.faas.myfavoritefilms.core.common.DomainError
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CompositeAnalyticsTest {
    private val immediate = UnconfinedTestDispatcher()

    private class RecordingTracker : AnalyticsTracker {
        val events = mutableListOf<AnalyticsEvent>()
        override fun track(event: AnalyticsEvent) {
            events += event
        }
    }

    private class RecordingErrorTracker : ErrorTracker {
        val errors = mutableListOf<Pair<Throwable, Map<String, String>>>()
        override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) {
            errors += throwable to attributes
        }
    }

    @Test
    fun `an event reaches every tracker`() {
        val a = RecordingTracker()
        val b = RecordingTracker()

        CompositeAnalytics(setOf(a, b), immediate).track(screenViewEvent(ScreenNames.MOVIES))

        assertEquals(listOf(screenViewEvent("movies")), a.events)
        assertEquals(a.events, b.events)
    }

    @Test
    fun `a failing tracker does not block the others`() {
        val broken = object : AnalyticsTracker {
            override fun track(event: AnalyticsEvent) = error("SDK unavailable")
        }
        val ok = RecordingTracker()

        CompositeAnalytics(setOf(broken, ok), immediate).track(searchEvent("matrix"))

        assertEquals(1, ok.events.size)
    }

    @Test
    fun `no registered trackers is a no-op`() {
        CompositeAnalytics(emptySet(), immediate).track(searchEvent("matrix"))
    }

    @Test
    fun `events follow the agreed names and params`() {
        assertEquals(
            AnalyticsEvent("favorite_added", mapOf("movie_id" to 603L, "source" to "detail")),
            favoriteToggledEvent(603, isFavorite = true, source = MovieSource.DETAIL),
        )
        assertEquals("favorite_removed", favoriteToggledEvent(603, false, MovieSource.POPULAR).name)
        assertEquals(100, (searchEvent("x".repeat(300)).params["search_term"] as String).length)
        assertEquals("false", searchResultsEvent("xyzw", firstPageCount = 0).params["has_results"])
        assertEquals("true", searchResultsEvent("matrix", firstPageCount = 20).params["has_results"])
        assertEquals(20L, searchResultsEvent("matrix", firstPageCount = 20).params["first_page_count"])
    }

    @Test
    fun `only unexpected errors become non fatal`() {
        val tracker = RecordingErrorTracker()
        val reporter = CompositeErrorReporter(setOf(tracker), immediate)

        reporter.reportIfUnexpected(IOException(), DomainError.Network, origin = "x")
        reporter.reportIfUnexpected(IOException(), DomainError.NotFound, origin = "x")
        assertTrue(tracker.errors.isEmpty())

        reporter.reportIfUnexpected(IllegalStateException(), DomainError.Server(503), origin = "movies_paging")
        reporter.reportIfUnexpected(IllegalStateException(), DomainError.Unknown, origin = "movie_detail")
        assertEquals(listOf("movies_paging", "movie_detail"), tracker.errors.map { it.second["origin"] })
    }

    @Test
    fun `tracking does not run on the caller and keeps the event order`() {
        val dispatcher = StandardTestDispatcher()
        val tracker = RecordingTracker()
        val analytics = CompositeAnalytics(setOf(tracker), dispatcher)

        analytics.track(screenViewEvent(ScreenNames.MOVIES))
        analytics.track(movieOpenedEvent(603, MovieSource.POPULAR))
        assertTrue(tracker.events.isEmpty())

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            listOf(screenViewEvent(ScreenNames.MOVIES), movieOpenedEvent(603, MovieSource.POPULAR)),
            tracker.events,
        )
    }

    @Test
    fun `error reporting does not run on the caller`() {
        val dispatcher = StandardTestDispatcher()
        val tracker = RecordingErrorTracker()
        val reporter = CompositeErrorReporter(setOf(tracker), dispatcher)

        reporter.recordNonFatal(IllegalStateException(), mapOf("origin" to "x"))
        assertTrue(tracker.errors.isEmpty())

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, tracker.errors.size)
    }
}
