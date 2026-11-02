import androidx.room.gradle.RoomExtension
import com.faas.myfavoritefilms.buildlogic.lib
import com.faas.myfavoritefilms.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("androidx.room")

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                add("api", libs.lib("androidx-room-runtime"))
                add("implementation", libs.lib("androidx-room-ktx"))
                add("ksp", libs.lib("androidx-room-compiler"))
            }
        }
    }
}
