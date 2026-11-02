import com.faas.myfavoritefilms.buildlogic.lib
import com.faas.myfavoritefilms.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class AndroidRobolectricConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.github.takahirom.roborazzi")

            tasks.withType<Test>().configureEach {
                jvmArgs(
                    "--add-opens=java.base/jdk.internal.access=ALL-UNNAMED",
                    "--add-opens=java.base/java.io=ALL-UNNAMED",
                    "--enable-native-access=ALL-UNNAMED",
                )
            }

            dependencies {
                add("testImplementation", libs.lib("robolectric"))
                add("testImplementation", libs.lib("androidx-test-core"))
                add("testImplementation", libs.lib("androidx-test-ext-junit"))
                add("testImplementation", libs.lib("roborazzi"))
                add("testImplementation", libs.lib("roborazzi-compose"))
                add("testImplementation", libs.lib("roborazzi-junit-rule"))
            }

            pluginManager.withPlugin("org.jetbrains.kotlin.plugin.compose") {
                dependencies {
                    add("testImplementation", platform(libs.lib("androidx-compose-bom")))
                    add("testImplementation", libs.lib("androidx-compose-ui-test-junit4"))
                }
            }
        }
    }
}
