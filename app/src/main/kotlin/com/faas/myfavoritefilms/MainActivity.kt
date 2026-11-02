package com.faas.myfavoritefilms

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.di.LocalViewModelFactory
import com.faas.myfavoritefilms.core.di.daggerViewModel
import com.faas.myfavoritefilms.core.ui.LocalAnalytics
import com.faas.myfavoritefilms.navigation.MoviesApp
import com.faas.myfavoritefilms.presentation.MainActivityUiState
import com.faas.myfavoritefilms.presentation.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appComponent = (application as MoviesApplication).appComponent

        setContent {
            CompositionLocalProvider(
                LocalViewModelFactory provides appComponent.viewModelFactory,
                LocalAnalytics provides appComponent.analytics,
            ) {
                ThemedApp()
            }
        }
    }

    @Composable
    private fun ThemedApp(viewModel: MainActivityViewModel = daggerViewModel()) {
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        val success = state as? MainActivityUiState.Success ?: return
        val darkTheme = success.useDarkTheme(isSystemInDarkTheme())

        DisposableEffect(darkTheme) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                navigationBarStyle = SystemBarStyle.auto(LIGHT_SCRIM, DARK_SCRIM) { darkTheme },
            )
            onDispose { }
        }

        MoviesTheme(darkTheme = darkTheme, dynamicColor = success.preferences.useDynamicColor) {
            MoviesApp()
        }
    }

    private companion object {
        val LIGHT_SCRIM = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
        val DARK_SCRIM = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
    }
}
