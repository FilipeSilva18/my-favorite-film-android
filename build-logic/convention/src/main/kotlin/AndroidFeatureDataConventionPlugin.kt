import com.faas.myfavoritefilms.buildlogic.lib
import com.faas.myfavoritefilms.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AndroidFeatureDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("movies.android.library")
            pluginManager.apply("movies.dagger")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                add("implementation", project(":core:common"))
                add("implementation", project(":core:network"))
                add("implementation", project(":core:analytics"))
                add("implementation", libs.lib("kotlinx-serialization-json"))

                add("testImplementation", project(":core:testing"))
                add("testImplementation", libs.lib("okhttp-mockwebserver"))
                add("testImplementation", libs.lib("retrofit-converter-kotlinx-serialization"))
            }
        }
    }
}
