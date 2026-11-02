plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.dagger)
}

android {
    namespace = "com.faas.myfavoritefilms.core.datastore"
}

dependencies {
    api(libs.androidx.datastore.preferences)
}
