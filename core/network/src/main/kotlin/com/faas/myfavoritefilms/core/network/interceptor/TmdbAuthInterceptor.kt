package com.faas.myfavoritefilms.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class TmdbAuthInterceptor @Inject constructor(
    @param:TmdbAccessToken private val accessToken: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .header("Accept", "application/json")
                .build(),
        )
}
