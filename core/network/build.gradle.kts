import java.util.Properties

plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.dagger)
    alias(libs.plugins.kotlin.serialization)
}

val tmdbAccessToken: String = run {
    val localFile = rootProject.layout.projectDirectory.file("local.properties")
    val fromLocal = providers.fileContents(localFile).asText.orNull
        ?.let { text -> Properties().apply { load(text.reader()) } }
        ?.getProperty("TMDB_ACCESS_TOKEN")
    val fromEnv = providers.environmentVariable("TMDB_ACCESS_TOKEN").orNull
    (fromLocal ?: fromEnv).orEmpty().trim()
}

if (tmdbAccessToken.isEmpty()) {
    logger.warn(
        "w: TMDB_ACCESS_TOKEN not found (local.properties or environment variable). " +
            "TMDB requests will fail with 401.",
    )
}

android {
    namespace = "com.faas.myfavoritefilms.core.network"
    buildFeatures { buildConfig = true }
    defaultConfig {
        buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
        buildConfigField(
            "String",
            "TMDB_ACCESS_TOKEN",
            "\"$tmdbAccessToken\"",
        )
    }
}

dependencies {
    api(projects.core.common)

    api(libs.retrofit.core)
    api(libs.okhttp.core)
    api(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging)

    testImplementation(libs.okhttp.mockwebserver)
}
