package com.faas.myfavoritefilms.core.analytics.firebase

import android.content.Context
import android.os.Bundle
import com.faas.myfavoritefilms.core.analytics.AnalyticsEvent
import com.faas.myfavoritefilms.core.analytics.AnalyticsTracker
import com.google.firebase.analytics.FirebaseAnalytics

internal class FirebaseAnalyticsTracker(context: Context) : AnalyticsTracker {
    private val firebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }

    override fun track(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.name, event.params.toBundle())
    }
}

internal fun Map<String, Any>.toBundle(): Bundle = Bundle().apply {
    forEach { (key, value) ->
        when (value) {
            is Int -> putLong(key, value.toLong())
            is Long -> putLong(key, value)
            is Double -> putDouble(key, value)
            is Float -> putDouble(key, value.toDouble())
            is Boolean -> putString(key, value.toString())
            else -> putString(key, value.toString())
        }
    }
}
