package com.faas.myfavoritefilms.di

import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faas.myfavoritefilms.MoviesApplication
import com.faas.myfavoritefilms.feature.favorites.presentation.FavoritesViewModel
import com.faas.myfavoritefilms.feature.moviedetail.presentation.MovieDetailViewModel
import com.faas.myfavoritefilms.feature.movies.presentation.MoviesViewModel
import com.faas.myfavoritefilms.feature.settings.presentation.SettingsViewModel
import com.faas.myfavoritefilms.presentation.MainActivityViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class DependencyGraphTest {
    private val app: MoviesApplication = ApplicationProvider.getApplicationContext()

    @Test
    fun `every app ViewModel is registered and can be created`() {
        val graph = app.appComponent.viewModelComponentFactory
            .create(SavedStateHandle(mapOf("movieId" to 603)))
        val providers = graph.viewModelProviders()

        assertEquals(
            setOf(
                MainActivityViewModel::class.java,
                MoviesViewModel::class.java,
                MovieDetailViewModel::class.java,
                FavoritesViewModel::class.java,
                SettingsViewModel::class.java,
            ),
            providers.keys,
        )
        providers.forEach { (viewModelClass, provider) ->
            assertTrue("${viewModelClass.simpleName} was not created", viewModelClass.isInstance(provider.get()))
        }
    }

    @Test
    fun `each ViewModel request gets a new instance`() {
        val graph = app.appComponent.viewModelComponentFactory.create(SavedStateHandle())
        val provider = graph.viewModelProviders().getValue(FavoritesViewModel::class.java)

        assertTrue(provider.get() !== provider.get())
    }

    @Test
    fun `app component is unique per process`() {
        assertTrue(app.appComponent === app.appComponent)
    }

    @Test
    fun `analytics is a singleton`() {
        assertTrue(app.appComponent.analytics === app.appComponent.analytics)
    }
}
