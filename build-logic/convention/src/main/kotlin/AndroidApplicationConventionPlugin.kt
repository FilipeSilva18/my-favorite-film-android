import com.android.build.api.dsl.ApplicationExtension
import com.faas.myfavoritefilms.buildlogic.configureKotlin
import com.faas.myfavoritefilms.buildlogic.int
import com.faas.myfavoritefilms.buildlogic.libs
import com.faas.myfavoritefilms.buildlogic.registerUnitTestTask
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import com.faas.myfavoritefilms.buildlogic.lib

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlinx.kover")

            extensions.configure<ApplicationExtension> {
                compileSdk = libs.int("compileSdk")
                defaultConfig {
                    minSdk = libs.int("minSdk")
                    targetSdk = libs.int("targetSdk")
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                    testInstrumentationRunnerArguments["clearPackageData"] = "true"
                }
                testOptions {
                    execution = "ANDROIDX_TEST_ORCHESTRATOR"
                    unitTests.isIncludeAndroidResources = true
                    unitTests.isReturnDefaultValues = true
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
            configureKotlin()
            registerUnitTestTask("testDebugUnitTest")

            dependencies {
                add("testImplementation", libs.lib("junit4"))
                add("testImplementation", libs.lib("kotlinx-coroutines-test"))
                add("androidTestImplementation", libs.lib("androidx-test-ext-junit"))
                add("androidTestImplementation", libs.lib("androidx-test-runner"))
                add("androidTestImplementation", libs.lib("androidx-test-rules"))
                add("androidTestUtil", libs.lib("androidx-test-orchestrator"))
            }
        }
    }
}
