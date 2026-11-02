package com.faas.myfavoritefilms.core.testing.repository

import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import com.faas.myfavoritefilms.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeUserPreferencesRepository(
    initial: UserPreferences = UserPreferences(),
) : UserPreferencesRepository {

    private val preferences = MutableStateFlow(initial)

    val current: UserPreferences get() = preferences.value

    override fun observeUserPreferences(): Flow<UserPreferences> = preferences

    override suspend fun setThemeMode(themeMode: ThemeMode) = preferences.update { it.copy(themeMode = themeMode) }

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) =
        preferences.update { it.copy(useDynamicColor = useDynamicColor) }

    override suspend fun setLanguage(language: AppLanguage) = preferences.update { it.copy(language = language) }
}
