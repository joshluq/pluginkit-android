package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Android Navigation Convention Plugin.
 *
 * Configures Navigation support, typically using Jetpack Navigation Compose.
 * Applies:
 * - `org.jetbrains.kotlin.plugin.serialization` (often used for type-safe navigation)
 *
 * Dependencies:
 * - `navigation` bundle (containing navigation-compose, hilt-navigation-compose, kotlinx-serialization, etc.)
 */
@Suppress("unused")
class AndroidNavigationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                add("implementation", libs.findBundle("navigation").get())
            }
        }
    }
}
