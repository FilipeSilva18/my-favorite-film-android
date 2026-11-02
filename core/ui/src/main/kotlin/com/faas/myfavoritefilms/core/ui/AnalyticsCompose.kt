package com.faas.myfavoritefilms.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.faas.myfavoritefilms.core.analytics.Analytics
import com.faas.myfavoritefilms.core.analytics.NoOpAnalytics
import com.faas.myfavoritefilms.core.analytics.screenViewEvent

val LocalAnalytics = staticCompositionLocalOf<Analytics> { NoOpAnalytics }

@Composable
fun TrackScreenView(screenName: String) {
    val analytics = LocalAnalytics.current
    DisposableEffect(screenName) {
        analytics.track(screenViewEvent(screenName))
        onDispose { }
    }
}
