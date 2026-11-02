package com.faas.myfavoritefilms.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

internal class TmdbLanguageInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter("language", Locale.getDefault().toLanguageTag())
            .build()
        return chain.proceed(request.newBuilder().url(url).build())
    }
}
