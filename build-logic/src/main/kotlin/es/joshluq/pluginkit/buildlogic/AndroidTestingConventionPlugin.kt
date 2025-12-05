package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Android Testing Convention Plugin.
 *
 * Configures dependencies for Unit Testing and Android Instrumentation Testing.
 *
 * Dependencies:
 * - `testing-unit` bundle (JUnit, MockK, etc.) for `testImplementation`.
 * - `testing-android` bundle (Espresso, Compose Test, etc.) for `androidTestImplementation`.
 * - Compose BOM for `androidTestImplementation` (to ensure version alignment).
 */
@Suppress("unused")
class AndroidTestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("testImplementation", libs.findBundle("testing-unit").get())
                add("androidTestImplementation", libs.findBundle("testing-android").get())
                add("androidTestImplementation", platform(libs.findLibrary("androidx-compose-bom").get()))
            }
        }
    }
}
