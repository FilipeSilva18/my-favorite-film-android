package com.faas.myfavoritefilms.core.data.repository

import com.faas.myfavoritefilms.core.datastore.UserPreferencesData
import com.faas.myfavoritefilms.core.datastore.UserPreferencesDataSource
import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import com.faas.myfavoritefilms.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: UserPreferencesDataSource,
) : UserPreferencesRepository {

    override fun observeUserPreferences(): Flow<UserPreferences> =
        dataSource.userPreferences.map { it.toDomain() }.distinctUntilChanged()

    override suspend fun setThemeMode(themeMode: ThemeMode) = dataSource.setThemeMode(themeMode.name)

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) = dataSource.setUseDynamicColor(useDynamicColor)

    override suspend fun setLanguage(language: AppLanguage) = dataSource.setLanguageTag(language.tag)
}

internal fun UserPreferencesData.toDomain(): UserPreferences {
    val defaults = UserPreferences()
    return UserPreferences(
        themeMode = ThemeMode.entries.firstOrNull { it.name == themeMode } ?: defaults.themeMode,
        useDynamicColor = useDynamicColor ?: defaults.useDynamicColor,
        language = languageTag?.let(AppLanguage::fromTag) ?: defaults.language,
    )
}
