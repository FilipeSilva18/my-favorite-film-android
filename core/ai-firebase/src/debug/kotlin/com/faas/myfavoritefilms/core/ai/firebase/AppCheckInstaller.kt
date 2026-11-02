package com.faas.myfavoritefilms.core.ai.firebase

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

object AppCheckInstaller {
    fun install(context: Context) {
        if (!context.isFirebaseConfigured()) return
        Firebase.appCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
    }
}
