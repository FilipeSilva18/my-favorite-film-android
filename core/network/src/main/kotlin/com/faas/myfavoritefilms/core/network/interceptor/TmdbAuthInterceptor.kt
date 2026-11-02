package com.faas.myfavoritefilms.core.network.interceptor

import com.faas.myfavoritefilms.core.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class TmdbAuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .header("Authorization", "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}")
                .header("Accept", "application/json")
                .build(),
        )
}
