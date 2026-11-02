package com.faas.myfavoritefilms.core.domain.usecase

import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
    private val repository: UserPreferencesRepository,
) {
    suspend operator fun invoke(themeMode: ThemeMode) = repository.setThemeMode(themeMode)
}
