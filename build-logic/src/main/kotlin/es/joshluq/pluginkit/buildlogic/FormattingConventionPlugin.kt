package es.joshluq.pluginkit.buildlogic

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Formatting Convention Plugin.
 *
 * Configures Spotless for code formatting.
 * Applies:
 * - `com.diffplug.spotless`
 *
 * Configures:
 * - Kotlin formatting using KtLint.
 * - Gradle Kotlin DSL script formatting using KtLint.
 * - Standard rules: trim trailing whitespace, spaces for tabs, end with newline.
 * - Suppressions for specific KtLint rules.
 */
@Suppress("unused")
class FormattingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.diffplug.spotless")

            extensions.configure<SpotlessExtension> {
                kotlin {
                    target("**/*.kt")
                    targetExclude("**/build/**/*.kt")
                    ktlint()
                    suppressLintsFor {
                        step = "ktlint"
                        shortCode = "standard:filename"
                    }
                    suppressLintsFor {
                        step = "ktlint"
                        shortCode = "standard:function-naming"
                    }
                    trimTrailingWhitespace()
                    leadingTabsToSpaces()
                    endWithNewline()
                }
                kotlinGradle {
                    target("*.gradle.kts")
                    ktlint()
                }
            }
        }
    }
}
