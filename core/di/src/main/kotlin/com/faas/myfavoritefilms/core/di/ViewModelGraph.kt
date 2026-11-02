package com.faas.myfavoritefilms.core.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.multibindings.Multibinds
import javax.inject.Provider

interface ViewModelGraph {
    fun viewModelProviders(): Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
}

fun interface ViewModelGraphFactory {
    fun create(savedStateHandle: SavedStateHandle): ViewModelGraph
}

@Module
interface ViewModelMultibindsModule {
    @Multibinds
    fun viewModels(): Map<Class<out ViewModel>, ViewModel>
}
