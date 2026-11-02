package com.faas.myfavoritefilms.feature.moviedetail.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.faas.myfavoritefilms.feature.moviedetail.presentation.MovieDetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailRoute(val movieId: Int)

internal const val MOVIE_ID_ARG = "movieId"

fun NavController.navigateToMovieDetail(movieId: Int, navOptions: NavOptions? = null) =
    navigate(MovieDetailRoute(movieId), navOptions)

fun NavGraphBuilder.movieDetailScreen(onBack: () -> Unit) {
    composable<MovieDetailRoute> {
        MovieDetailScreen(onBack = onBack)
    }
}
