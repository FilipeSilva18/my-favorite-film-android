package com.faas.myfavoritefilms.feature.moviedetail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.faas.myfavoritefilms.core.designsystem.component.BACKDROP_ASPECT_RATIO
import com.faas.myfavoritefilms.core.designsystem.component.MoviePoster
import com.faas.myfavoritefilms.core.designsystem.component.RatingBadge
import com.faas.myfavoritefilms.core.designsystem.component.SkeletonBox
import com.faas.myfavoritefilms.feature.moviedetail.presentation.R
import com.faas.myfavoritefilms.feature.moviedetail.presentation.model.MovieDetailUi

internal const val MOVIE_DETAIL_SKELETON_TEST_TAG = "movieDetailSkeleton"

private val PosterWidth = 112.dp
private val PosterOverlap = 56.dp

private fun Modifier.overlapUp(amount: Dp): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val overlap = amount.roundToPx()
    layout(placeable.width, (placeable.height - overlap).coerceAtLeast(0)) {
        placeable.place(0, -overlap)
    }
}

@Composable
internal fun MovieDetailHeader(movie: MovieDetailUi, modifier: Modifier = Modifier) {
    Column(modifier) {
        Box {
            MoviePoster(
                url = movie.backdropUrl,
                contentDescription = movie.title,
                aspectRatio = BACKDROP_ASPECT_RATIO,
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth(),
            )

            Box(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0.5f to Color.Transparent,
                            1f to MaterialTheme.colorScheme.background,
                        ),
                    ),
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 16.dp).overlapUp(PosterOverlap),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            MoviePoster(
                url = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier.width(PosterWidth),
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(movie.title, style = MaterialTheme.typography.headlineSmall)
                movie.tagline?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = yearAndRuntime(movie.year, movie.runtimeMinutes),
                    style = MaterialTheme.typography.labelLarge,
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RatingBadge(label = movie.ratingLabel)
                    Text(
                        text = stringResource(R.string.detail_votes, movie.voteCountLabel),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
internal fun MovieDetailSkeleton(modifier: Modifier = Modifier) {
    Column(modifier.testTag(MOVIE_DETAIL_SKELETON_TEST_TAG)) {
        SkeletonBox(
            Modifier.fillMaxWidth().height(220.dp),
            shape = RectangleShape,
        )
        Row(
            modifier = Modifier.padding(horizontal = 16.dp).overlapUp(PosterOverlap),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            SkeletonBox(Modifier.width(PosterWidth).height(PosterWidth * 1.5f), MaterialTheme.shapes.medium)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SkeletonBox(Modifier.fillMaxWidth(0.8f).height(24.dp))
                SkeletonBox(Modifier.fillMaxWidth(0.5f).height(16.dp))
                SkeletonBox(Modifier.width(80.dp).height(16.dp))
            }
        }
        Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(4) { SkeletonBox(Modifier.fillMaxWidth().height(14.dp)) }
            SkeletonBox(Modifier.fillMaxWidth(0.6f).height(14.dp))
        }
    }
}

@Composable
private fun yearAndRuntime(year: String?, runtimeMinutes: Int?): String {
    val runtime = runtimeMinutes?.let { runtimeText(it) }
    return when {
        year != null && runtime != null -> stringResource(R.string.detail_year_runtime, year, runtime)
        else -> year ?: runtime.orEmpty()
    }
}

@Composable
private fun runtimeText(minutes: Int): String {
    val hours = minutes / MINUTES_PER_HOUR
    val rest = minutes % MINUTES_PER_HOUR
    return when {
        hours == 0 -> stringResource(R.string.detail_runtime_minutes, rest)
        rest == 0 -> stringResource(R.string.detail_runtime_hours, hours)
        else -> stringResource(R.string.detail_runtime_hours_minutes, hours, rest)
    }
}

private const val MINUTES_PER_HOUR = 60
