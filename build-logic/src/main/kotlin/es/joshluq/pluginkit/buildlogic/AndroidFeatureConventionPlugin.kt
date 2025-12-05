package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Android Feature Convention Plugin.
 *
 * A composite plugin that sets up a full-featured Android Library module.
 * Intended for Feature modules in a modular architecture.
 *
 * Applies the following plugins:
 * - `pluginkit.android.library`: Base Android Library setup.
 * - `pluginkit.android.hilt`: Dependency Injection setup.
 * - `pluginkit.android.compose`: Jetpack Compose UI setup.
 * - `pluginkit.coroutines`: Coroutines support.
 * - `pluginkit.android.navigation`: Navigation support.
 */
@Suppress("unused")
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("pluginkit.android.library")
            pluginManager.apply("pluginkit.android.hilt")
            pluginManager.apply("pluginkit.android.compose")
            pluginManager.apply("pluginkit.coroutines")
            pluginManager.apply("pluginkit.android.navigation")
        }
    }
}
