package com.faas.myfavoritefilms.core.testing.analytics

import com.faas.myfavoritefilms.core.analytics.Analytics
import com.faas.myfavoritefilms.core.analytics.AnalyticsEvent
import com.faas.myfavoritefilms.core.analytics.ErrorReporter

class TestAnalytics : Analytics {
    val events = mutableListOf<AnalyticsEvent>()

    override fun track(event: AnalyticsEvent) {
        events += event
    }

    fun eventsNamed(name: String) = events.filter { it.name == name }
}

class TestErrorReporter : ErrorReporter {
    val errors = mutableListOf<Pair<Throwable, Map<String, String>>>()

    override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) {
        errors += throwable to attributes
    }
}
