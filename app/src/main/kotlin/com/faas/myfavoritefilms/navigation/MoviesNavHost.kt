package com.faas.myfavoritefilms.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.faas.myfavoritefilms.feature.favorites.presentation.navigation.favoritesScreen
import com.faas.myfavoritefilms.feature.moviedetail.presentation.navigation.movieDetailScreen
import com.faas.myfavoritefilms.feature.moviedetail.presentation.navigation.navigateToMovieDetail
import com.faas.myfavoritefilms.feature.movies.presentation.navigation.MoviesRoute
import com.faas.myfavoritefilms.feature.movies.presentation.navigation.moviesScreen
import com.faas.myfavoritefilms.feature.settings.presentation.navigation.settingsScreen

@Composable
fun MoviesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MoviesRoute,
        modifier = modifier,
    ) {
        moviesScreen(onMovieClick = navController::navigateToMovieDetail)
        favoritesScreen(onMovieClick = navController::navigateToMovieDetail)
        movieDetailScreen(onBack = navController::navigateUp)
        settingsScreen()
    }
}
