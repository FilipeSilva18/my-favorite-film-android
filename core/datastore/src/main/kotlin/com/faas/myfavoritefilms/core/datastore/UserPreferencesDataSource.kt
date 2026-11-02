package com.faas.myfavoritefilms.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val userPreferences: Flow<UserPreferencesData> = dataStore.data
        .catch { error -> if (error is IOException) emit(emptyPreferences()) else throw error }
        .map { preferences ->
            UserPreferencesData(
                themeMode = preferences[Keys.THEME_MODE],
                useDynamicColor = preferences[Keys.USE_DYNAMIC_COLOR],
                languageTag = preferences[Keys.LANGUAGE_TAG],
            )
        }

    suspend fun setThemeMode(themeMode: String) {
        dataStore.edit { it[Keys.THEME_MODE] = themeMode }
    }

    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        dataStore.edit { it[Keys.USE_DYNAMIC_COLOR] = useDynamicColor }
    }

    suspend fun setLanguageTag(languageTag: String) {
        dataStore.edit { it[Keys.LANGUAGE_TAG] = languageTag }
    }

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val LANGUAGE_TAG = stringPreferencesKey("language_tag")
    }
}
