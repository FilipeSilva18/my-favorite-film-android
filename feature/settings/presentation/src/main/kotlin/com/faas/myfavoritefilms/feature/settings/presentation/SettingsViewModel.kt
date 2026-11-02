package com.faas.myfavoritefilms.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faas.myfavoritefilms.core.analytics.Analytics
import com.faas.myfavoritefilms.core.analytics.settingsChangedEvent
import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.usecase.ObserveUserPreferencesUseCase
import com.faas.myfavoritefilms.core.domain.usecase.SetAppLanguageUseCase
import com.faas.myfavoritefilms.core.domain.usecase.SetDynamicColorUseCase
import com.faas.myfavoritefilms.core.domain.usecase.SetThemeModeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    observeUserPreferences: ObserveUserPreferencesUseCase,
    private val setThemeMode: SetThemeModeUseCase,
    private val setDynamicColor: SetDynamicColorUseCase,
    private val setAppLanguage: SetAppLanguageUseCase,
    private val analytics: Analytics,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = observeUserPreferences()
        .map<_, SettingsUiState> { SettingsUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading,
        )

    fun onThemeModeSelected(themeMode: ThemeMode) {
        viewModelScope.launch {
            setThemeMode(themeMode)
            analytics.track(settingsChangedEvent(SETTING_THEME_MODE, themeMode.name.lowercase()))
        }
    }

    fun onDynamicColorChanged(useDynamicColor: Boolean) {
        viewModelScope.launch {
            setDynamicColor(useDynamicColor)
            analytics.track(settingsChangedEvent(SETTING_DYNAMIC_COLOR, useDynamicColor.toString()))
        }
    }

    fun onLanguageSelected(language: AppLanguage) {
        viewModelScope.launch {
            analytics.track(settingsChangedEvent(SETTING_LANGUAGE, language.tag.ifEmpty { SYSTEM_VALUE }))
            setAppLanguage(language)
        }
    }

    internal companion object {
        const val SETTING_THEME_MODE = "theme_mode"
        const val SETTING_DYNAMIC_COLOR = "dynamic_color"
        const val SETTING_LANGUAGE = "language"
        const val SYSTEM_VALUE = "system"
    }
}
