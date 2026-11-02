plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.android.compose)
    alias(libs.plugins.movies.dagger)
}

android {
    namespace = "com.faas.myfavoritefilms.core.di"
}

dependencies {
    api(libs.dagger)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.lifecycle.viewmodel.savedstate)
}
