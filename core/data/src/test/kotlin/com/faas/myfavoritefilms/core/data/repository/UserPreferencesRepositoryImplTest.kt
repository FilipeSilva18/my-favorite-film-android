package com.faas.myfavoritefilms.core.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.cash.turbine.test
import com.faas.myfavoritefilms.core.datastore.UserPreferencesData
import com.faas.myfavoritefilms.core.datastore.UserPreferencesDataSource
import com.faas.myfavoritefilms.core.domain.model.AppLanguage
import com.faas.myfavoritefilms.core.domain.model.ThemeMode
import com.faas.myfavoritefilms.core.domain.model.UserPreferences
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class UserPreferencesRepositoryImplTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private fun TestScope.repository() = UserPreferencesRepositoryImpl(
        UserPreferencesDataSource(
            PreferenceDataStoreFactory.create(
                scope = backgroundScope,
                produceFile = { File(tempFolder.root, "prefs.preferences_pb") },
            ),
        ),
    )

    @Test
    fun `defaults when nothing was saved and round trips every preference`() = runTest {
        val repository = repository()

        repository.observeUserPreferences().test {
            assertEquals(UserPreferences(), awaitItem())

            repository.setThemeMode(ThemeMode.DARK)
            assertEquals(ThemeMode.DARK, awaitItem().themeMode)

            repository.setUseDynamicColor(false)
            assertEquals(false, awaitItem().useDynamicColor)

            repository.setLanguage(AppLanguage.ENGLISH)
            assertEquals(UserPreferences(ThemeMode.DARK, false, AppLanguage.ENGLISH), awaitItem())
        }
    }

    @Test
    fun `unknown stored values fall back to defaults`() {
        assertEquals(
            UserPreferences(),
            UserPreferencesData(themeMode = "NEON", useDynamicColor = null, languageTag = "fr").toDomain(),
        )
    }
}
