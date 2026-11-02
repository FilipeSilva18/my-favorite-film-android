package com.faas.myfavoritefilms.di

import android.util.Log
import com.faas.myfavoritefilms.core.analytics.AnalyticsEvent
import com.faas.myfavoritefilms.core.analytics.AnalyticsTracker
import com.faas.myfavoritefilms.core.analytics.ErrorTracker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import javax.inject.Inject

@Module
interface BuildTypeAnalyticsModule {
    @Binds
    @IntoSet
    fun bindLogcatAnalyticsTracker(impl: LogcatAnalyticsTracker): AnalyticsTracker

    @Binds
    @IntoSet
    fun bindLogcatErrorTracker(impl: LogcatErrorTracker): ErrorTracker
}

class LogcatAnalyticsTracker @Inject constructor() : AnalyticsTracker {
    override fun track(event: AnalyticsEvent) {
        Log.d(TAG, "${event.name} ${event.params}")
    }
}

class LogcatErrorTracker @Inject constructor() : ErrorTracker {
    override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) {
        Log.w(TAG, "non-fatal $attributes", throwable)
    }
}

private const val TAG = "Analytics"
