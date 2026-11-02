package com.faas.myfavoritefilms.core.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.core.common.DomainError
import com.faas.myfavoritefilms.core.designsystem.theme.MoviesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class StateContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `domain error shows its message and a retry button`() {
        var retries = 0
        composeRule.setContent {
            MoviesTheme { ErrorContent(error = DomainError.Unauthorized, onRetry = { retries++ }) }
        }

        composeRule.onNodeWithText(context.getString(R.string.error_unauthorized)).assertExists()
        composeRule.onNodeWithText(context.getString(R.string.action_retry)).performClick()

        assertEquals(1, retries)
    }

    @Test
    fun `every error maps to a different string`() {
        val errors = listOf(
            DomainError.Network, DomainError.Unauthorized, DomainError.NotFound,
            DomainError.RateLimited, DomainError.Server(500), DomainError.Unknown,
        )
        assertEquals(errors.size, errors.map { it.messageRes() }.toSet().size)
    }

    @Test
    fun `empty content shows the message`() {
        composeRule.setContent { MoviesTheme { EmptyContent(message = "Nothing here") } }

        composeRule.onNodeWithText("Nothing here").assertExists()
    }
}
