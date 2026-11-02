package com.faas.myfavoritefilms.core.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.cash.turbine.test
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class UserPreferencesDataSourceTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private fun TestScope.dataSource() = UserPreferencesDataSource(
        PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder.root, "test.preferences_pb") },
        ),
    )

    @Test
    fun `starts empty and persists every field`() = runTest {
        val dataSource = dataSource()

        dataSource.userPreferences.test {
            assertEquals(UserPreferencesData(), awaitItem())

            dataSource.setThemeMode("DARK")
            assertEquals("DARK", awaitItem().themeMode)

            dataSource.setUseDynamicColor(false)
            assertEquals(false, awaitItem().useDynamicColor)

            dataSource.setLanguageTag("en")
            assertEquals(UserPreferencesData("DARK", false, "en"), awaitItem())
        }
    }
}
