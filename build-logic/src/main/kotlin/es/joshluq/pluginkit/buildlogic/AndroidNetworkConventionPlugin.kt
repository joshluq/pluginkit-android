package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Android Network Convention Plugin.
 *
 * Configures Networking libraries.
 *
 * Dependencies:
 * - `network` bundle (typically Retrofit, OkHttp, Converters).
 */
@Suppress("unused")
class AndroidNetworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", libs.findBundle("network").get())
            }
        }
    }
}
