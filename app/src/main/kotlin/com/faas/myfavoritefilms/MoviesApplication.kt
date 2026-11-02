package com.faas.myfavoritefilms

import android.app.Application
import com.faas.myfavoritefilms.core.ai.firebase.AppCheckInstaller
import com.faas.myfavoritefilms.di.AppComponent
import com.faas.myfavoritefilms.di.DaggerAppComponent

class MoviesApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        AppCheckInstaller.install(this)
    }
}
