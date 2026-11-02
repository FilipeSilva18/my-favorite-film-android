plugins {
    alias(libs.plugins.movies.jvm.library)
    alias(libs.plugins.movies.dagger)
    `java-test-fixtures`
}

dependencies {
    api(projects.core.common)
    api(projects.feature.favorites.api)
    testImplementation(libs.turbine)
}
