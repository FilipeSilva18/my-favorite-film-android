import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.faas.myfavoritefilms.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions { jvmTarget = JvmTarget.JVM_17 }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.movies.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.movies.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = libs.plugins.movies.android.compose.get().pluginId
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.movies.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("dagger") {
            id = libs.plugins.movies.dagger.get().pluginId
            implementationClass = "DaggerConventionPlugin"
        }
        register("androidRoom") {
            id = libs.plugins.movies.android.room.get().pluginId
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidFeatureData") {
            id = libs.plugins.movies.android.feature.data.get().pluginId
            implementationClass = "AndroidFeatureDataConventionPlugin"
        }
        register("androidRobolectric") {
            id = libs.plugins.movies.android.robolectric.get().pluginId
            implementationClass = "AndroidRobolectricConventionPlugin"
        }
        register("androidFeaturePresentation") {
            id = libs.plugins.movies.android.feature.presentation.get().pluginId
            implementationClass = "AndroidFeaturePresentationConventionPlugin"
        }
    }
}
