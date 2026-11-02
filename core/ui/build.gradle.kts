plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.android.compose)
    alias(libs.plugins.movies.android.robolectric)
}

android {
    namespace = "com.faas.myfavoritefilms.core.ui"
}

dependencies {
    api(projects.core.designsystem)
    api(projects.core.common)
    api(projects.core.analytics)
}
