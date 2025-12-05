package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Hilt Convention Plugin.
 *
 * Configures Dependency Injection using Dagger Hilt.
 * Applies:
 * - `org.jetbrains.kotlin.kapt`
 * - `com.google.dagger.hilt.android`
 *
 * Dependencies:
 * - `hilt-android` (implementation)
 * - `hilt-compiler` (kapt)
 */
@Suppress("unused")
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.kapt")
                apply("com.google.dagger.hilt.android")
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt-android").get())
                add("kapt", libs.findLibrary("hilt-compiler").get())
            }
        }
    }
}
