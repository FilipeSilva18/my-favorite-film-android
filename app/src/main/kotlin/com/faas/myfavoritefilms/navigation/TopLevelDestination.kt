package com.faas.myfavoritefilms.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.faas.myfavoritefilms.R
import com.faas.myfavoritefilms.feature.favorites.presentation.navigation.FavoritesRoute
import com.faas.myfavoritefilms.feature.movies.presentation.navigation.MoviesRoute
import com.faas.myfavoritefilms.feature.settings.presentation.navigation.SettingsRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val route: KClass<*>,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val labelRes: Int,
) {
    MOVIES(MoviesRoute::class, Icons.Filled.Home, Icons.Outlined.Home, R.string.nav_movies),
    FAVORITES(FavoritesRoute::class, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, R.string.nav_favorites),
    SETTINGS(SettingsRoute::class, Icons.Filled.Settings, Icons.Outlined.Settings, R.string.nav_settings),
}
