plugins {
    alias(libs.plugins.movies.android.feature.presentation)
}

android {
    namespace = "com.faas.myfavoritefilms.feature.favorites.presentation"
}

dependencies {
    api(projects.feature.favorites.domain)
    testImplementation(testFixtures(projects.feature.favorites.domain))
    testImplementation(libs.turbine)
}
