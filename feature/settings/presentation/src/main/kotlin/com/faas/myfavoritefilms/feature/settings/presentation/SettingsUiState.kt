package com.faas.myfavoritefilms.feature.settings.presentation

import com.faas.myfavoritefilms.core.domain.model.UserPreferences

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val preferences: UserPreferences) : SettingsUiState
}
