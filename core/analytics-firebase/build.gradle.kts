plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.dagger)
}

android {
    namespace = "com.faas.myfavoritefilms.core.analytics.firebase"
}

dependencies {
    api(projects.core.analytics)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}
