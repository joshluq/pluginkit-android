package es.joshluq.pluginkit.buildlogic

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import java.net.URI

@Suppress("unused")
open class AndroidPublishingExtension {
    var repoName: String? = null
    var repoUrl: String? = null
    var repoUser: String? = null
    var repoPassword: String? = null
    var version: String? = null
    var groupId: String? = null
    var artifactId: String? = null
}

/**
 * Android Publishing Convention Plugin.
 *
 * Configures Maven publishing for Android Library modules.
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
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.create("androidPublishing", AndroidPublishingExtension::class.java)

            pluginManager.apply("maven-publish")
            extensions.configure<LibraryExtension> {
                publishing {
                    singleVariant("release") {
                        withSourcesJar()
                    }
                }
            }
            afterEvaluate {
                val repoNameValue = extension.repoName ?: "MavenRepo"
                val repoUrlValue = extension.repoUrl
                val repoUserValue = extension.repoUser
                val repoPasswordValue = extension.repoPassword
                val versionValue = extension.version
                val groupIdValue = extension.groupId
                val artifactIdValue = extension.artifactId

                extensions.configure<PublishingExtension> {
                    publications {
                        create<MavenPublication>("release") {
                            val releaseComponent = components.findByName("release")
                            from(releaseComponent)

                            groupId = groupIdValue
                            artifactId = artifactIdValue
                            version = versionValue

                            pom {
                                name.set(artifactIdValue ?:"Android Library" )
                                description.set("Android library published automatically")
                            }
                        }
                    }

                    repositories {
                        repoUrlValue?.let {
                            maven {
                                name = repoNameValue
                                url = URI.create(repoUrlValue)

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
