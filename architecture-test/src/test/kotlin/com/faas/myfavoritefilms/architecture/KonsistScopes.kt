package com.faas.myfavoritefilms.architecture

import com.lemonappdev.konsist.api.Konsist

internal val productionScope by lazy {
    Konsist.scopeFromProduction()
        .files
        .filter { file ->
            file.packagee?.name?.startsWith(BASE_PACKAGE) == true &&
                !file.path.contains("/build-logic/") &&
                !file.path.contains("/architecture-test/")
        }
}

internal const val BASE_PACKAGE = "com.faas.myfavoritefilms"
internal val FEATURES = listOf("movies", "moviedetail", "favorites", "settings")
