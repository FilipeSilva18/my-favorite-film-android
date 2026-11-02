package com.faas.myfavoritefilms.core.analytics

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class CompositeAnalytics @Inject constructor(
    private val trackers: Set<@JvmSuppressWildcards AnalyticsTracker>,
    @AnalyticsDispatcher dispatcher: CoroutineDispatcher,
) : Analytics {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    override fun track(event: AnalyticsEvent) {
        scope.launch {
            trackers.forEach { tracker -> runCatching { tracker.track(event) } }
        }
    }
}

internal class CompositeErrorReporter @Inject constructor(
    private val trackers: Set<@JvmSuppressWildcards ErrorTracker>,
    @AnalyticsDispatcher dispatcher: CoroutineDispatcher,
) : ErrorReporter {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) {
        scope.launch {
            trackers.forEach { tracker -> runCatching { tracker.recordNonFatal(throwable, attributes) } }
        }
    }
}
