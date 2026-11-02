plugins {
    alias(libs.plugins.movies.android.feature.presentation)
}

android {
    namespace = "com.faas.myfavoritefilms.feature.moviedetail.presentation"
}

dependencies {
    api(projects.feature.moviedetail.domain)
    testImplementation(testFixtures(projects.feature.favorites.api))
}
