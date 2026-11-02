plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.dagger)
    alias(libs.plugins.movies.android.robolectric)
}

android {
    namespace = "com.faas.myfavoritefilms.feature.favorites.data"
}

dependencies {
    api(projects.feature.favorites.domain)
    implementation(projects.core.database)
    testImplementation(libs.turbine)
}
