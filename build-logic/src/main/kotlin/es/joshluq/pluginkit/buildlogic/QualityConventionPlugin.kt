package es.joshluq.pluginkit.buildlogic

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.sonarqube.gradle.SonarExtension

interface PluginKitQualityExtension {
    var sonarHost: Property<String>
    var sonarProjectKey: Property<String>
    var koverExclusions: ListProperty<String>
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
 * - Kover with custom exclusions.
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
                toolVersion = "1.23.8"
                source.setFrom(files("src/main/java", "src/main/kotlin"))
                config.setFrom(files("${project.rootDir}/config/detekt/detekt.yml"))
                buildUponDefaultConfig = true
                allRules = false
                autoCorrect = true
                baseline = file("$projectDir/config/baseline.xml")
            }

            tasks.withType<Detekt>().configureEach {
                reports {
                    html.required.set(true)
                    sarif.required.set(true)
                }
            }

            afterEvaluate {
                extensions.configure<SonarExtension> {
                    properties {
                        property("sonar.sourceEncoding", "UTF-8")
                        property("sonar.verbose", "true")

                        qualityExtension.sonarHost.orNull.let { property("sonar.host.url", it) }
                        qualityExtension.sonarProjectKey.orNull.let { property("sonar.projectKey", it) }
                    }
                }

                extensions.configure<KoverProjectExtension> {
                    reports {
                        filters {
                            excludes {
                                classes(qualityExtension.koverExclusions.getOrElse(emptyList()))
                            }
                        }
                    }
                }
            }

            dependencies {
                add("detektPlugins", libs.findLibrary("detekt-formatting").get())
            }
        }
    }
}
