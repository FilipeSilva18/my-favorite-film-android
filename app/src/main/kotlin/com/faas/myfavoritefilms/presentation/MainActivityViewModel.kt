package com.faas.myfavoritefilms.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faas.myfavoritefilms.core.di.ViewModelKey
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import com.faas.myfavoritefilms.core.domain.usecase.ObserveUserPreferencesUseCase
import com.faas.myfavoritefilms.core.domain.usecase.SyncAppLanguageUseCase
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    observeUserPreferences: ObserveUserPreferencesUseCase,
    private val syncAppLanguage: SyncAppLanguageUseCase,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = observeUserPreferences()
        .map<_, MainActivityUiState> { MainActivityUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainActivityUiState.Loading,
        )

    init {
        viewModelScope.launch { syncAppLanguage() }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val preferences: UserPreferences) : MainActivityUiState {
        fun useDarkTheme(isSystemInDarkTheme: Boolean): Boolean = when (preferences.themeMode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
        }
    }
}

@Module
interface AppViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel
}
