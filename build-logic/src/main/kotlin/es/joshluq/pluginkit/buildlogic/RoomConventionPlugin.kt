package es.joshluq.pluginkit.buildlogic

import androidx.room.gradle.RoomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Room Convention Plugin.
 *
 * Configures Local Persistence using Room.
 * Applies:
 * - `com.google.devtools.ksp`
 * - `androidx.room`
 *
 * Dependencies:
 * - `room-runtime` (implementation)
 * - `room-ktx` (implementation)
 * - `room-compiler` (ksp)
 */
@Suppress("unused")
class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("androidx.room")
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx-room-runtime").get())
                add("implementation", libs.findLibrary("androidx-room-ktx").get())
                add("ksp", libs.findLibrary("androidx-room-compiler").get())
            }
        }
    }
}
