package com.faas.myfavoritefilms.core.analytics.firebase

import com.faas.myfavoritefilms.core.analytics.ErrorTracker
import com.google.firebase.crashlytics.FirebaseCrashlytics

internal class CrashlyticsErrorTracker : ErrorTracker {
    private val crashlytics by lazy { FirebaseCrashlytics.getInstance() }

    override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) {
        attributes.forEach { (key, value) -> crashlytics.setCustomKey(key, value) }
        crashlytics.recordException(throwable)
    }
}
