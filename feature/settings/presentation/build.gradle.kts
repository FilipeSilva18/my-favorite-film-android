plugins {
    alias(libs.plugins.movies.android.feature.presentation)
}

android {
    namespace = "com.faas.myfavoritefilms.feature.settings.presentation"
}

dependencies {
    api(projects.core.domain)
}
