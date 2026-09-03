package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import java.net.URI

@Suppress("unused")
open class JvmPublishingExtension {
    var repoName: String? = null
    var repoUrl: String? = null
    var repoUser: String? = null
    var repoPassword: String? = null
    var version: String? = null
    var groupId: String? = null
    var artifactId: String? = null
    var pomName: String? = null
    var pomDescription: String? = null
}

/**
 * JVM Publishing Convention Plugin.
 *
 * Configures Maven publishing for pure Kotlin/Java JVM library modules.
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
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.create("jvmPublishing", JvmPublishingExtension::class.java)

            pluginManager.apply("maven-publish")

            // Ensure sources and javadoc jars are attached to the java component
            extensions.configure<JavaPluginExtension> {
                withSourcesJar()
                withJavadocJar()
            }

            afterEvaluate {
                val repoNameValue = extension.repoName ?: "MavenRepo"
                val repoUrlValue = extension.repoUrl
                    ?: System.getenv("MAVEN_REPO_URL")
                    ?: System.getenv("REPO_URL")
                val repoUserValue = extension.repoUser
                    ?: System.getenv("MAVEN_REPO_USER")
                    ?: System.getenv("REPO_USER")
                    ?: System.getenv("GITHUB_ACTOR")
                val repoPasswordValue = extension.repoPassword
                    ?: System.getenv("MAVEN_REPO_PASSWORD")
                    ?: System.getenv("REPO_PASSWORD")
                    ?: System.getenv("GITHUB_TOKEN")

                val versionValue = extension.version
                    ?: project.version.toString().takeIf { it != Project.DEFAULT_VERSION }
                val groupIdValue = extension.groupId
                    ?: project.group.toString().takeIf { it.isNotBlank() }
                val artifactIdValue = extension.artifactId ?: project.name

                extensions.configure<PublishingExtension> {
                    publications {
                        create<MavenPublication>("mavenJava") {
                            components.findByName("java")?.let { javaComponent ->
                                from(javaComponent)
                            }

                            groupIdValue?.let { groupId = it }
                            artifactIdValue.let { artifactId = it }
                            versionValue?.let { version = it }

                            pom {
                                name.set(extension.pomName ?: artifactIdValue)
                                description.set(extension.pomDescription ?: "Kotlin JVM library published automatically")
                            }
                        }
                    }

                    repositories {
                        repoUrlValue?.takeIf { it.isNotBlank() }?.let { rawUrl ->
                            maven {
                                name = repoNameValue
                                url = URI.create(rawUrl)

                                if (!repoUserValue.isNullOrBlank() || !repoPasswordValue.isNullOrBlank()) {
                                    credentials {
                                        username = repoUserValue ?: ""
                                        password = repoPasswordValue ?: ""
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
