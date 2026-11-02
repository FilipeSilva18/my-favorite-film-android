package com.faas.myfavoritefilms.core.ui.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme

object MovieGridDefaults {
    val Columns = GridCells.Adaptive(minSize = 140.dp)
    val ContentPadding = PaddingValues(12.dp)
    val Spacing = 12.dp
    const val SKELETON_ITEMS = 9
    const val SKELETON_TEST_TAG = "movieGridSkeleton"
}

@Composable
fun MovieGrid(
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = MovieGridDefaults.ContentPadding,
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit,
) {
    LazyVerticalGrid(
        columns = MovieGridDefaults.Columns,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(MovieGridDefaults.Spacing),
        verticalArrangement = Arrangement.spacedBy(MovieGridDefaults.Spacing),
        userScrollEnabled = userScrollEnabled,
        modifier = modifier.fillMaxSize(),
        content = content,
    )
}

@Composable
fun MovieGridSkeleton(modifier: Modifier = Modifier) {
    MovieGrid(modifier = modifier.testTag(MovieGridDefaults.SKELETON_TEST_TAG), userScrollEnabled = false) {
        items(count = MovieGridDefaults.SKELETON_ITEMS, contentType = { "skeleton" }) {
            MovieCardSkeleton()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieGridSkeletonPreview() {
    MoviesTheme { MovieGridSkeleton() }
}
