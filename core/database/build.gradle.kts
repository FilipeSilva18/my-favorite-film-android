plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.android.room)
    alias(libs.plugins.movies.dagger)
}

android {
    namespace = "com.faas.myfavoritefilms.core.database"
}

dependencies {
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.turbine)
}
