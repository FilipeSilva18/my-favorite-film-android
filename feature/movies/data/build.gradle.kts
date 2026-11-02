plugins {
    alias(libs.plugins.movies.android.feature.data)
}

android {
    namespace = "com.faas.myfavoritefilms.feature.movies.data"
}

dependencies {
    api(projects.feature.movies.domain)
    implementation(projects.core.ai)
    testImplementation(libs.androidx.paging.testing)
}
