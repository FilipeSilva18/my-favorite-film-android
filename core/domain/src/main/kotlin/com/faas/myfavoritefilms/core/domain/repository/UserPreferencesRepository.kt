package com.faas.myfavoritefilms.core.domain.repository

import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun observeUserPreferences(): Flow<UserPreferences>
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
    suspend fun setLanguage(language: AppLanguage)
}
