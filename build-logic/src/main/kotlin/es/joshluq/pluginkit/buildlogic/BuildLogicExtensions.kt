package es.joshluq.pluginkit.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun CommonExtension<*, *, *, *, *, *>.configureCompose(project: Project) {
    buildFeatures {
        compose = true
    }

    project.dependencies {
        val bom = project.libs.findLibrary("androidx-compose-bom").get()
        add("implementation", enforcedPlatform(bom))
        add("androidTestImplementation", platform(bom))

        add("implementation", project.libs.findBundle("compose").get())
        
        add("debugImplementation", project.libs.findLibrary("androidx-compose-ui-tooling").get())
        add("debugImplementation", project.libs.findLibrary("androidx-compose-ui-test-manifest").get())
    }
}
