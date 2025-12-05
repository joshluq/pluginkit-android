package es.joshluq.pluginkit.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

/**
 * JVM Library Convention Plugin.
 *
 * Configures a pure Kotlin/Java library (non-Android).
 * Applies:
 * - `org.jetbrains.kotlin.jvm`
 *
 * Sets up:
 * - Java compatibility (Version 11).
 */
@Suppress("unused")
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }
    }
}
