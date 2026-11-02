plugins {
    alias(libs.plugins.movies.android.library)
    alias(libs.plugins.movies.dagger)
}

android {
    namespace = "com.faas.myfavoritefilms.core.ai.firebase"
}

dependencies {
    api(projects.core.ai)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ai)
    implementation(libs.firebase.appcheck)
    debugImplementation(libs.firebase.appcheck.debug)
    releaseImplementation(libs.firebase.appcheck.playintegrity)
}
