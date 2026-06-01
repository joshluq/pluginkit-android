package es.joshluq.pluginkit.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType

/**
 * Android Compose Convention Plugin.
 *
 * Configures Jetpack Compose for Android modules (Application or Library).
 * Applies:
 * - `org.jetbrains.kotlin.plugin.compose`
 *
 * Configures:
 * - `buildFeatures.compose = true`
 * - Compose BOM and related UI libraries (ui, graphics, tooling-preview, material3).
 * - Debug tooling dependencies.
 */
@Suppress("unused")
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.findByType<ApplicationExtension>()
                ?: extensions.findByType<LibraryExtension>()
            
            (extension as? CommonExtension)?.apply {

                buildFeatures.compose = true

                target.dependencies {
                    val bom = project.libs.findLibrary("androidx-compose-bom").get()
                    add("implementation", platform(bom))
                    add("androidTestImplementation", platform(bom))

                    add("implementation", project.libs.findBundle("compose").get())

                    add("debugImplementation", project.libs.findLibrary("androidx-compose-ui-tooling").get())
                    add("debugImplementation", project.libs.findLibrary("androidx-compose-ui-test-manifest").get())
                }
            }
        }
    }
}
