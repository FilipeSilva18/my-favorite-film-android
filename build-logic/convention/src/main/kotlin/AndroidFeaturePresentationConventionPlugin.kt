import com.faas.myfavoritefilms.buildlogic.bundle
import com.faas.myfavoritefilms.buildlogic.lib
import com.faas.myfavoritefilms.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AndroidFeaturePresentationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("movies.android.library")
            pluginManager.apply("movies.android.compose")
            pluginManager.apply("movies.dagger")
            pluginManager.apply("movies.android.robolectric")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                add("implementation", project(":core:common"))
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:di"))
                add("implementation", project(":core:analytics"))

                add("implementation", libs.bundle("lifecycle-compose"))
                add("implementation", libs.lib("androidx-navigation-compose"))

                add("testImplementation", project(":core:testing"))
            }
        }
    }
}
