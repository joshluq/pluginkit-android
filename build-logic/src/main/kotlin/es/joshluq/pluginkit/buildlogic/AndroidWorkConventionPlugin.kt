package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Android WorkManager Convention Plugin.
 *
 * Configures WorkManager and its Hilt integration.
 * Applies:
 * - `com.google.devtools.ksp`
 *
 * Dependencies:
 * - `androidx-work-runtime` (implementation)
 * - `androidx-hilt-work` (implementation)
 * - `hilt-compiler-androidx` (ksp)
 */
@Suppress("unused")
class AndroidWorkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx-work-runtime").get())
                add("implementation", libs.findLibrary("androidx-hilt-work").get())
                add("ksp", libs.findLibrary("hilt-compiler-androidx").get())
            }
        }
    }
}
