plugins {
    alias(libs.plugins.movies.jvm.library)
    alias(libs.plugins.movies.dagger)
}

dependencies {
    api(projects.core.common)
    api(projects.feature.favorites.api)
    testImplementation(projects.core.testing)
}
