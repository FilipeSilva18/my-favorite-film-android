plugins {
    alias(libs.plugins.movies.android.feature.presentation)
}

android {
    namespace = "com.faas.myfavoritefilms.feature.movies.presentation"
}

dependencies {
    api(projects.feature.movies.domain)
    testImplementation(testFixtures(projects.feature.favorites.api))
    implementation(libs.androidx.paging.compose)
    testImplementation(libs.androidx.paging.testing)
}
