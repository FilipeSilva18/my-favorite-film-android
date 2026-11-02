plugins {
    alias(libs.plugins.movies.jvm.library)
}

dependencies {
    api(projects.core.domain)
    api(projects.core.analytics)
    api(libs.junit4)
    api(libs.kotlinx.coroutines.test)
}
