package com.faas.myfavoritefilms.core.analytics.di

import com.faas.myfavoritefilms.core.analytics.Analytics
import com.faas.myfavoritefilms.core.analytics.AnalyticsDispatcher
import com.faas.myfavoritefilms.core.analytics.AnalyticsTracker
import com.faas.myfavoritefilms.core.analytics.CompositeAnalytics
import com.faas.myfavoritefilms.core.analytics.CompositeErrorReporter
import com.faas.myfavoritefilms.core.analytics.ErrorReporter
import com.faas.myfavoritefilms.core.analytics.ErrorTracker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.Multibinds
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module(includes = [AnalyticsBindings::class, AnalyticsDispatcherModule::class])
object AnalyticsModule

@Module
internal interface AnalyticsBindings {
    @Binds
    @Singleton
    fun bindAnalytics(impl: CompositeAnalytics): Analytics

    @Binds
    @Singleton
    fun bindErrorReporter(impl: CompositeErrorReporter): ErrorReporter

    @Multibinds
    fun analyticsTrackers(): Set<AnalyticsTracker>

    @Multibinds
    fun errorTrackers(): Set<ErrorTracker>
}

@Module
internal object AnalyticsDispatcherModule {
    @Provides
    @Singleton
    @AnalyticsDispatcher
    fun provideAnalyticsDispatcher(): CoroutineDispatcher = Dispatchers.IO.limitedParallelism(1)
}
