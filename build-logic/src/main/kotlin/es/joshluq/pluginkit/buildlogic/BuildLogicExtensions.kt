package es.joshluq.pluginkit.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.getIntVersion(alias: String, default: Int): Int {
    return libs.findVersion(alias)
        .map { it.requiredVersion.toIntOrNull() ?: default }
        .orElse(default)
}

