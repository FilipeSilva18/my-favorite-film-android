package com.faas.myfavoritefilms.core.ai.firebase

import android.content.Context
import com.faas.myfavoritefilms.core.ai.AiJsonGenerator
import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AiFirebaseModule {
    @Provides
    @Singleton
    fun provideAiJsonGenerator(context: Context): AiJsonGenerator =
        if (context.isFirebaseConfigured()) GeminiJsonGenerator() else UnavailableAiJsonGenerator()
}

internal fun Context.isFirebaseConfigured(): Boolean =
    runCatching { FirebaseApp.getApps(this).isNotEmpty() }.getOrDefault(false)
