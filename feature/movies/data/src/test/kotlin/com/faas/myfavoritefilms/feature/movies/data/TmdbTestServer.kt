package com.faas.myfavoritefilms.feature.movies.data

import com.faas.myfavoritefilms.core.network.TmdbJson
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal fun MockWebServer.tmdbRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl(url("/3/"))
    .addConverterFactory(TmdbJson.asConverterFactory("application/json".toMediaType()))
    .build()

internal fun MockWebServer.enqueueResource(path: String, code: Int = 200) {
    val body = checkNotNull(Fixtures::class.java.classLoader?.getResource(path)) { "fixture not found: $path" }.readText()
    enqueue(MockResponse.Builder().code(code).body(body).build())
}

internal fun MockWebServer.enqueueError(code: Int) {
    enqueue(MockResponse.Builder().code(code).body("""{"status_code":7,"success":false}""").build())
}

private object Fixtures
