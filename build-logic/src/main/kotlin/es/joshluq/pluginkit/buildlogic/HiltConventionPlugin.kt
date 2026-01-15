package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Hilt Convention Plugin.
 *
 * Configures Dependency Injection using Dagger Hilt.
 * Applies:
 * - `com.google.devtools.ksp`
 * - `com.google.dagger.hilt.android`
 *
 * Dependencies:
 * - `hilt-android` (implementation)
 * - `hilt-compiler` (ksp)
 */
@Suppress("unused")
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt-android").get())
                add("ksp", libs.findLibrary("hilt-compiler").get())
            }
        }
    }
}
