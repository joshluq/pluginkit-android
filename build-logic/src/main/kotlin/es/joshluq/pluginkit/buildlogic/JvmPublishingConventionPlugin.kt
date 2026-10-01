package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import java.net.URI

interface JvmPublishingExtension {
    val repoName: Property<String>
    val repoUrl: Property<String>
    val repoUser: Property<String>
    val repoPassword: Property<String>
    val version: Property<String>
    val groupId: Property<String>
    val artifactId: Property<String>
    val pomName: Property<String>
    val pomDescription: Property<String>
}

/**
 * JVM Publishing Convention Plugin.
 *
 * Configures Maven publishing for pure Kotlin/Java JVM library modules using Gradle's Lazy Configuration API.
 * Applies:
 * - `maven-publish`
 *
 * Configures:
 * - Sources JAR generation via [JavaPluginExtension.withSourcesJar].
 * - Javadoc JAR generation via [JavaPluginExtension.withJavadocJar].
 * - Publication of the 'java' component with POM metadata.
 * - Target Maven repository credentials and URL via `jvmPublishing` extension
 *   or CI environment variables fallback (MAVEN_REPO_URL, GITHUB_TOKEN, etc.).
 */
@Suppress("unused")
class JvmPublishingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val extension = extensions.create<JvmPublishingExtension>("jvmPublishing").apply {
            repoName.convention("MavenRepo")
            artifactId.convention(provider { project.name })
            groupId.convention(provider { project.group.toString().takeIf { it.isNotBlank() } ?: "es.joshluq.kit" })
            version.convention(provider { project.version.toString().takeIf { it != Project.DEFAULT_VERSION } ?: "1.0.0" })
            pomName.convention(artifactId)
            pomDescription.convention("Kotlin JVM library published automatically")
        }

        pluginManager.apply("maven-publish")

        // Ensure sources and javadoc jars are attached to the java component
        extensions.configure<JavaPluginExtension> {
            withSourcesJar()
            withJavadocJar()
        }

        extensions.configure<PublishingExtension> {
            publications {
                create<MavenPublication>("mavenJava") {
                    val javaComponent = components.findByName("java")
                    if (javaComponent != null) {
                        from(javaComponent)
                    }

                    groupId = extension.groupId.get()
                    artifactId = extension.artifactId.get()
                    version = extension.version.get()

                    pom {
                        name.set(extension.pomName)
                        description.set(extension.pomDescription)
                    }
                }
            }

            repositories {
                maven {
                    name = extension.repoName.get()
                    url = URI.create(
                        extension.repoUrl.orNull
                            ?: System.getenv("MAVEN_REPO_URL")
                            ?: System.getenv("REPO_URL")
                            ?: "https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY") ?: "joshluq/pluginkit-android"}"
                    )

                    credentials {
                        username = extension.repoUser.orNull
                            ?: System.getenv("MAVEN_REPO_USER")
                            ?: System.getenv("REPO_USER")
                            ?: System.getenv("GITHUB_ACTOR")
                            ?: ""
                        password = extension.repoPassword.orNull
                            ?: System.getenv("MAVEN_REPO_PASSWORD")
                            ?: System.getenv("REPO_PASSWORD")
                            ?: System.getenv("GITHUB_TOKEN")
                            ?: ""
                    }
                }
            }
        }
    }
}
