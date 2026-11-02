package com.faas.myfavoritefilms.core.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import javax.inject.Inject

class DaggerViewModelFactory @Inject constructor(
    private val graphFactory: ViewModelGraphFactory,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = runCatching { extras.createSavedStateHandle() }
            .getOrElse { SavedStateHandle() }
        val providers = graphFactory.create(savedStateHandle).viewModelProviders()
        val provider = providers[modelClass]
            ?: providers.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: error(
                "${modelClass.name} is not registered. Add a @Binds @IntoMap " +
                    "@ViewModelKey(${modelClass.simpleName}::class) and include its module in the :app ViewModelComponent.",
            )
        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }
}
