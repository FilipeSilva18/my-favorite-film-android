package com.faas.myfavoritefilms.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES_FILE) },
        )

    private const val USER_PREFERENCES_FILE = "user_preferences"
}
