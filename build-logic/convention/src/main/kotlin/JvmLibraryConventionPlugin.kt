import com.faas.myfavoritefilms.buildlogic.bundle
import com.faas.myfavoritefilms.buildlogic.configureKotlin
import com.faas.myfavoritefilms.buildlogic.lib
import com.faas.myfavoritefilms.buildlogic.libs
import com.faas.myfavoritefilms.buildlogic.registerUnitTestTask
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("java-library")
            pluginManager.apply("org.jetbrains.kotlin.jvm")
            pluginManager.apply("org.jetbrains.kotlinx.kover")

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            configureKotlin()
            registerUnitTestTask("test")

            dependencies {
                add("implementation", libs.lib("javax-inject"))

                add("api", libs.lib("kotlinx-coroutines-core"))
                add("testImplementation", libs.bundle("unit-test"))
            }
        }
    }
}
