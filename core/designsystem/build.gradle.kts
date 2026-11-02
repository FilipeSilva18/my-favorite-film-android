plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.android.compose)
    alias(libs.plugins.movies.android.robolectric)
}

android {
    namespace = "com.faas.myfavoritefilms.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.core)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
