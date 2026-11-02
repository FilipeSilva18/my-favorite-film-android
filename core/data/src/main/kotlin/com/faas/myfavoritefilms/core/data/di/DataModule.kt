package com.faas.myfavoritefilms.core.data.di

import com.faas.myfavoritefilms.core.data.repository.UserPreferencesRepositoryImpl
import com.faas.myfavoritefilms.core.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [DataBindsModule::class])
object DataModule

@Module
internal interface DataBindsModule {
    @Binds
    @Singleton
    fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository
}
