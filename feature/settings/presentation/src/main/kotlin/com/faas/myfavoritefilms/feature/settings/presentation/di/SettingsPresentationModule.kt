package com.faas.myfavoritefilms.feature.settings.presentation.di

import androidx.lifecycle.ViewModel
import com.faas.myfavoritefilms.core.di.ViewModelKey
import com.faas.myfavoritefilms.feature.settings.presentation.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SettingsPresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}
