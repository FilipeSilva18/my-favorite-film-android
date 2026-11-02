import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.faas.myfavoritefilms.buildlogic.bundle
import com.faas.myfavoritefilms.buildlogic.lib
import com.faas.myfavoritefilms.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> { buildFeatures.compose = true }
            }
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> { buildFeatures.compose = true }
            }

            dependencies {
                val bom = platform(libs.lib("androidx-compose-bom"))
                add("implementation", bom)
                add("androidTestImplementation", bom)
                add("implementation", libs.bundle("compose-ui"))
                add("debugImplementation", libs.lib("androidx-compose-ui-tooling"))
                add("debugImplementation", libs.lib("androidx-compose-ui-test-manifest"))
                add("androidTestImplementation", libs.lib("androidx-compose-ui-test-junit4"))
            }
        }
    }
}
