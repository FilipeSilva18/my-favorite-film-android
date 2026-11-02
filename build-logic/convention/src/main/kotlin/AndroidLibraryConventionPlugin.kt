import com.android.build.api.dsl.LibraryExtension
import com.faas.myfavoritefilms.buildlogic.configureKotlin
import com.faas.myfavoritefilms.buildlogic.int
import com.faas.myfavoritefilms.buildlogic.lib
import com.faas.myfavoritefilms.buildlogic.libs
import com.faas.myfavoritefilms.buildlogic.registerUnitTestTask
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlinx.kover")

            extensions.configure<LibraryExtension> {
                compileSdk = libs.int("compileSdk")
                defaultConfig {
                    minSdk = libs.int("minSdk")
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
                testOptions.unitTests.isReturnDefaultValues = true
                testOptions.unitTests.isIncludeAndroidResources = true
            }
            configureKotlin()
            registerUnitTestTask("testDebugUnitTest")

            dependencies {
                add("implementation", libs.lib("kotlinx-coroutines-android"))
                add("testImplementation", libs.lib("junit4"))
                add("testImplementation", libs.lib("kotlinx-coroutines-test"))
                add("testImplementation", libs.lib("turbine"))
                add("androidTestImplementation", libs.lib("androidx-test-ext-junit"))
                add("androidTestImplementation", libs.lib("androidx-test-runner"))
            }
        }
    }
}
