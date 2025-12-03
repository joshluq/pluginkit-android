package es.joshluq.pluginkit.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.getByType<CommonExtension<*, *, *, *, *>>()
            extension.apply {
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = "1.5.1"
                }
            }
        }
    }
}
