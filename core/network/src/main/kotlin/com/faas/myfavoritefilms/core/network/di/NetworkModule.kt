package com.faas.myfavoritefilms.core.network.di

import com.faas.myfavoritefilms.core.network.BuildConfig
import com.faas.myfavoritefilms.core.network.TmdbJson
import com.faas.myfavoritefilms.core.network.interceptor.TmdbAuthInterceptor
import com.faas.myfavoritefilms.core.network.interceptor.TmdbLanguageInterceptor
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [NetworkProvidersModule::class])
object NetworkModule

@Module
internal object NetworkProvidersModule {
    @Provides
    @Singleton
    fun provideJson(): Json = TmdbJson

    @Provides
    @Singleton
    fun provideOkHttpClient(
        auth: TmdbAuthInterceptor,
        language: TmdbLanguageInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(auth)
        .addInterceptor(language)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                        redactHeader("Authorization")
                    },
                )
            }
        }
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.TMDB_BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}
