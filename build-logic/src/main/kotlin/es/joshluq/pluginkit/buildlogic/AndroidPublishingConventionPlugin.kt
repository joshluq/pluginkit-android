package es.joshluq.pluginkit.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import java.net.URI

interface AndroidPublishingExtension {
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
 * Android Publishing Convention Plugin.
 *
 * Configures Maven publishing for Android Library modules using Gradle's Lazy Configuration API.
 * Applies:
 * - `maven-publish`
 *
 * Configures:
 * - Publication of the 'release' component with sources jar.
 * - POM generation with metadata (groupId, artifactId, version).
 * - Target repository configuration via `androidPublishing` extension.
 */
@Suppress("unused")
class AndroidPublishingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val extension = extensions.create<AndroidPublishingExtension>("androidPublishing").apply {
            repoName.convention("MavenRepo")
            artifactId.convention(provider { project.name })
            groupId.convention(provider { project.group.toString().takeIf { it.isNotBlank() } ?: "es.joshluq.kit" })
            version.convention(provider { project.version.toString().takeIf { it != Project.DEFAULT_VERSION } ?: "1.0.0" })
            pomName.convention(artifactId)
            pomDescription.convention("Android library published automatically")
        }

        pluginManager.apply("maven-publish")

        extensions.configure<LibraryExtension> {
            publishing {
                singleVariant("release") {
                    withSourcesJar()
                }
            }
        }

        extensions.configure<PublishingExtension> {
            publications {
                create<MavenPublication>("release") {
                    val releaseComponent = components.findByName("release")
                    if (releaseComponent != null) {
                        from(releaseComponent)
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

            // Repositories are configured lazily when repoUrl is present
            repositories {
                maven {
                    name = extension.repoName.get()
                    // Si se especifica repoUrl, se asigna; de lo contrario se apunta a mavenLocal o fallback seguro
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
