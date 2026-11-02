plugins {
    alias(libs.plugins.movies.jvm.library)
    alias(libs.plugins.movies.dagger)
}

dependencies {
    api(projects.core.common)
    testImplementation(projects.core.testing)
}
