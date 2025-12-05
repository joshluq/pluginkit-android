package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Coroutines Convention Plugin.
 *
 * Configures Kotlin Coroutines dependencies.
 *
 * Dependencies:
 * - `coroutines` bundle (kotlinx-coroutines-core, kotlinx-coroutines-android).
 */
@Suppress("unused")
class CoroutinesConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", libs.findBundle("coroutines").get())
            }
        }
    }
}
