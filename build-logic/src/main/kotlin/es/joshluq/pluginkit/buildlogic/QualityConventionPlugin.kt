package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.configure
import org.sonarqube.gradle.SonarExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

open class PluginKitQualityExtension {
    var sonarHost: String? = null
    var sonarToken: String? = null
    var sonarProjectKey: String? = null
    var sonarOrganization: String? = null
}

/**
 * Quality Convention Plugin.
 *
 * Configures code quality tools: Detekt, SonarQube, and Kover.
 * Applies:
 * - `io.gitlab.arturbosch.detekt`
 * - `org.sonarqube`
 * - `org.jetbrains.kotlinx.kover`
 *
 * Configures:
 * - Detekt with a default config file and auto-correction.
 * - SonarQube properties via `pluginkitQuality` extension.
 */
@Suppress("unused")
class QualityConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val qualityExtension = extensions.create<PluginKitQualityExtension>("pluginkitQuality")

            pluginManager.apply("io.gitlab.arturbosch.detekt")
            pluginManager.apply("org.sonarqube")
            pluginManager.apply("org.jetbrains.kotlinx.kover")

            extensions.configure<DetektExtension> {
                toolVersion = "1.23.7"
                source.setFrom(files("src/main/java", "src/main/kotlin"))
                config.setFrom(files("${project.rootDir}/config/detekt/detekt.yml"))
                buildUponDefaultConfig = true
                allRules = false
                autoCorrect = true
            }

            afterEvaluate {
                extensions.configure<SonarExtension> {
                    properties {
                        property("sonar.sourceEncoding", "UTF-8")
                        property("sonar.verbose", "true")

                        qualityExtension.sonarHost?.let { property("sonar.host.url", it) }
                        qualityExtension.sonarToken?.let { property("sonar.login", it) }
                        qualityExtension.sonarProjectKey?.let { property("sonar.projectKey", it) }
                        qualityExtension.sonarOrganization?.let { property("sonar.organization", it) }
                    }
                }
            }
        }
    }
}
