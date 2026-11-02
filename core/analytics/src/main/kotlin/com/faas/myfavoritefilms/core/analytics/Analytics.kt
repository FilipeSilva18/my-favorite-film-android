package com.faas.myfavoritefilms.core.analytics

interface Analytics {
    fun track(event: AnalyticsEvent)
}

interface AnalyticsTracker {
    fun track(event: AnalyticsEvent)
}

object NoOpAnalytics : Analytics {
    override fun track(event: AnalyticsEvent) = Unit
}
