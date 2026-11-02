package com.faas.myfavoritefilms.core.ai.firebase

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

object AppCheckInstaller {
    fun install(context: Context) {
        if (!context.isFirebaseConfigured()) return
        Firebase.appCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
    }
}
