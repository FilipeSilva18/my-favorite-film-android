package com.faas.myfavoritefilms.core.analytics.firebase

import android.content.Context
import com.faas.myfavoritefilms.core.analytics.AnalyticsTracker
import com.faas.myfavoritefilms.core.analytics.ErrorTracker
import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet

@Module
object FirebaseModule {
    @Provides
    @ElementsIntoSet
    fun provideFirebaseAnalyticsTrackers(context: Context): Set<AnalyticsTracker> =
        if (context.isFirebaseConfigured()) setOf(FirebaseAnalyticsTracker(context)) else emptySet()

    @Provides
    @ElementsIntoSet
    fun provideCrashlyticsErrorTrackers(context: Context): Set<ErrorTracker> =
        if (context.isFirebaseConfigured()) setOf(CrashlyticsErrorTracker()) else emptySet()

    private fun Context.isFirebaseConfigured(): Boolean =
        runCatching { FirebaseApp.getApps(this).isNotEmpty() }.getOrDefault(false)
}
