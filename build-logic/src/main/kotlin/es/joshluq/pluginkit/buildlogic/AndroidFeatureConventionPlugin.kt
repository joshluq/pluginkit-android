package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

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
