package com.faas.myfavoritefilms

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.faas.myfavoritefilms.feature.favorites.presentation.R as FavoritesR
import com.faas.myfavoritefilms.feature.movies.presentation.R as MoviesR

@RunWith(AndroidJUnit4::class)
@LargeTest
class MoviesAppNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun string(id: Int) = composeRule.activity.getString(id)

    private fun tab(label: Int) = composeRule.onNode(hasText(string(label)) and isSelectable())

    @Test
    fun opensOnMoviesTabWithSearchField() {
        tab(R.string.nav_movies).assertIsSelected()
        composeRule.onNodeWithText(string(MoviesR.string.movies_search_placeholder), useUnmergedTree = true).assertExists()
    }

    @Test
    fun navigatesToFavoritesShowsEmptyStateAndBack() {
        tab(R.string.nav_favorites).performClick()

        tab(R.string.nav_favorites).assertIsSelected()
        composeRule.onNodeWithText(string(FavoritesR.string.favorites_empty)).assertExists()

        tab(R.string.nav_movies).performClick()
        composeRule.onNodeWithText(string(MoviesR.string.movies_search_placeholder), useUnmergedTree = true).assertExists()
    }
}
