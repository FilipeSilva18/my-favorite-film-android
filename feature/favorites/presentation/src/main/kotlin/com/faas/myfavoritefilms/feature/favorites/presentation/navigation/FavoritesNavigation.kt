package com.faas.myfavoritefilms.feature.favorites.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.faas.myfavoritefilms.feature.favorites.presentation.FavoritesScreen
import kotlinx.serialization.Serializable

@Serializable
data object FavoritesRoute

fun NavController.navigateToFavorites(navOptions: NavOptions? = null) =
    navigate(FavoritesRoute, navOptions)

fun NavGraphBuilder.favoritesScreen(onMovieClick: (movieId: Int) -> Unit) {
    composable<FavoritesRoute> {
        FavoritesScreen(onMovieClick = onMovieClick)
    }
}
