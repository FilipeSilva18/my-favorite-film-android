package com.faas.myfavoritefilms.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.faas.myfavoritefilms.core.ai.firebase.AiFirebaseModule
import com.faas.myfavoritefilms.core.analytics.Analytics
import com.faas.myfavoritefilms.core.analytics.di.AnalyticsModule
import com.faas.myfavoritefilms.core.analytics.firebase.FirebaseModule
import com.faas.myfavoritefilms.core.data.di.DataModule
import com.faas.myfavoritefilms.core.database.di.DatabaseModule
import com.faas.myfavoritefilms.core.datastore.di.DataStoreModule
import com.faas.myfavoritefilms.core.domain.language.AppLanguageApplier
import com.faas.myfavoritefilms.core.network.di.NetworkModule
import com.faas.myfavoritefilms.feature.favorites.data.di.FavoritesDataModule
import com.faas.myfavoritefilms.feature.moviedetail.data.di.MovieDetailDataModule
import com.faas.myfavoritefilms.feature.movies.data.di.MoviesDataModule
import com.faas.myfavoritefilms.language.AppCompatLanguageApplier
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        DataStoreModule::class,
        DataModule::class,

        AnalyticsModule::class,
        FirebaseModule::class,
        AiFirebaseModule::class,
        BuildTypeAnalyticsModule::class,
        ViewModelBindingModule::class,

        MoviesDataModule::class,
        MovieDetailDataModule::class,
        FavoritesDataModule::class,
    ],
)
interface AppComponent {
    val viewModelFactory: ViewModelProvider.Factory

    val analytics: Analytics

    val viewModelComponentFactory: ViewModelComponent.Factory


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}

@Module
interface AppModule {
    @Binds
    fun bindContext(application: Application): Context

    @Binds
    fun bindAppLanguageApplier(impl: AppCompatLanguageApplier): AppLanguageApplier
}
