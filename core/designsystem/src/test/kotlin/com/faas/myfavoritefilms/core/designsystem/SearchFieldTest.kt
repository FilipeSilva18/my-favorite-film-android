package com.faas.myfavoritefilms.core.designsystem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.designsystem.component.SearchField
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class SearchFieldTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val changes = mutableListOf<String>()

    private fun setContent(isLoading: Boolean = false) {
        composeRule.setContent {
            var query by remember { mutableStateOf("") }
            MoviesTheme {
                SearchField(
                    query = query,
                    onQueryChange = { query = it; changes += it },
                    placeholder = "Buscar",
                    isLoading = isLoading,
                    clearContentDescription = "Limpar",
                )
            }
        }
    }

    @Test
    fun `typing sends the text`() {
        setContent()

        composeRule.onNode(hasSetTextAction()).performTextInput("matrix")

        assertEquals("matrix", changes.last())
    }

    @Test
    fun `clear button only shows with text and clears the field`() {
        setContent()
        composeRule.onNodeWithContentDescription("Limpar").assertDoesNotExist()

        composeRule.onNode(hasSetTextAction()).performTextInput("duna")
        composeRule.onNodeWithContentDescription("Limpar").performClick()

        assertEquals("", changes.last())
        composeRule.onNodeWithContentDescription("Limpar").assertDoesNotExist()
    }

    @Test
    fun `loading replaces the search icon with a spinner`() {
        setContent(isLoading = true)

        composeRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }
}
