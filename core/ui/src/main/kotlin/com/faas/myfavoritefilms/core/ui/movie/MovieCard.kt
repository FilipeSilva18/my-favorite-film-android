package com.faas.myfavoritefilms.core.ui.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faas.myfavoritefilms.core.designsystem.component.FavoriteIconButton
import com.faas.myfavoritefilms.core.designsystem.component.MoviePoster
import com.faas.myfavoritefilms.core.designsystem.component.POSTER_ASPECT_RATIO
import com.faas.myfavoritefilms.core.designsystem.component.RatingBadge
import com.faas.myfavoritefilms.core.designsystem.component.SkeletonBox
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import com.faas.myfavoritefilms.core.ui.R

@Composable
fun MovieCard(
    movie: MovieCardUi,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier) {
        Box {
            MoviePoster(
                url = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier.fillMaxWidth(),
            )
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
            ) {
                FavoriteIconButton(
                    isFavorite = movie.isFavorite,
                    onClick = onToggleFavorite,
                    contentDescription = stringResource(
                        if (movie.isFavorite) R.string.movie_favorite_remove else R.string.movie_favorite_add,
                    ),
                )
            }
        }
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = movie.year.orEmpty(), style = MaterialTheme.typography.labelMedium)
                RatingBadge(label = movie.ratingLabel)
            }
        }
    }
}

@Composable
fun MovieCardSkeleton(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        SkeletonBox(
            modifier = Modifier.fillMaxWidth().aspectRatio(POSTER_ASPECT_RATIO),
            shape = MaterialTheme.shapes.medium,
        )
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SkeletonBox(Modifier.fillMaxWidth().height(14.dp))
            SkeletonBox(Modifier.fillMaxWidth(0.6f).height(14.dp))
            Spacer(Modifier.height(2.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SkeletonBox(Modifier.width(36.dp).height(12.dp))
                SkeletonBox(Modifier.width(40.dp).height(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun MovieCardPreview() {
    MoviesTheme {
        MovieCard(
            movie = MovieCardUi(603, "Matrix", null, "1999", 8.2, "8.2", isFavorite = true),
            onClick = {},
            onToggleFavorite = {},
            modifier = Modifier.width(160.dp),
        )
    }
}

@Preview
@Composable
private fun MovieCardSkeletonPreview() {
    MoviesTheme { MovieCardSkeleton(Modifier.width(160.dp)) }
}
