plugins {
    alias(libs.plugins.movies.android.feature.data)
}

android {
    namespace = "com.faas.myfavoritefilms.feature.moviedetail.data"
}

dependencies {
    api(projects.feature.moviedetail.domain)
}
