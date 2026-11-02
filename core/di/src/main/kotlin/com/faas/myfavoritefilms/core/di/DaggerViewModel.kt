package com.faas.myfavoritefilms.core.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

val LocalViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("LocalViewModelFactory was not provided. Wrap the content with CompositionLocalProvider in MainActivity.")
}

@Composable
inline fun <reified VM : ViewModel> daggerViewModel(key: String? = null): VM =
    viewModel(key = key, factory = LocalViewModelFactory.current)
