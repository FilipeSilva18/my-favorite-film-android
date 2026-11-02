package com.faas.myfavoritefilms.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme

private enum class ImageLoadState { Loading, Success, Fallback }

@Composable
fun MoviePoster(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    aspectRatio: Float = POSTER_ASPECT_RATIO,
    shape: Shape = MaterialTheme.shapes.medium,
) {
    var loadState by remember(url) {
        mutableStateOf(if (url == null) ImageLoadState.Fallback else ImageLoadState.Loading)
    }

    Box(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        when (loadState) {
            ImageLoadState.Loading -> SkeletonBox(Modifier.matchParentSize(), RectangleShape)
            ImageLoadState.Fallback -> Text(
                text = contentDescription?.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ImageLoadState.Success -> Unit
        }
        if (url != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(url)
                    .crossfade(CROSSFADE_MS)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                onLoading = { loadState = ImageLoadState.Loading },
                onSuccess = { loadState = ImageLoadState.Success },
                onError = { loadState = ImageLoadState.Fallback },
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}

const val POSTER_ASPECT_RATIO = 2f / 3f
const val BACKDROP_ASPECT_RATIO = 16f / 9f
private const val CROSSFADE_MS = 300

@Preview
@Composable
private fun MoviePosterPreview() {
    MoviesTheme { MoviePoster(url = null, contentDescription = "Matrix") }
}
