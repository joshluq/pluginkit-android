package es.joshluq.pluginkit.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

@Suppress("unused")
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.findByType<ApplicationExtension>()
                ?: extensions.findByType<LibraryExtension>()
            
            (extension as? CommonExtension<*, *, *, *, *, *>)?.configureCompose(this)
        }
    }
}
