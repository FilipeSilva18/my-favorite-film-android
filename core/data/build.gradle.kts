plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.dagger)
    alias(libs.plugins.movies.android.robolectric)
}

android {
    namespace = "com.faas.myfavoritefilms.core.data"
}

dependencies {
    api(projects.core.domain)
    implementation(projects.core.datastore)
    testImplementation(projects.core.testing)
}
