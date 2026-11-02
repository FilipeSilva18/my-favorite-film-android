plugins {
    alias(libs.plugins.movies.android.application)
    alias(libs.plugins.movies.android.compose)
    alias(libs.plugins.movies.dagger)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.movies.android.robolectric)
}

if (file("google-services.json").exists()) {
    apply(plugin = libs.plugins.google.services.get().pluginId)
    apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
}

android {
    namespace = "com.faas.myfavoritefilms"

    defaultConfig {
        applicationId = "com.faas.myfavoritefilms"
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
}

dependencies {
    implementation(projects.core.di)
    implementation(projects.core.network)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.data)
    implementation(projects.core.analyticsFirebase)
    implementation(projects.core.aiFirebase)

    implementation(projects.core.designsystem)
    implementation(projects.core.ui)

    implementation(projects.feature.movies.presentation)
    implementation(projects.feature.movies.data)
    implementation(projects.feature.moviedetail.presentation)
    implementation(projects.feature.moviedetail.data)
    implementation(projects.feature.favorites.presentation)
    implementation(projects.feature.favorites.data)
    implementation(projects.feature.settings.presentation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.lifecycle.compose)

    testImplementation(projects.core.testing)
    testImplementation(libs.turbine)
}
