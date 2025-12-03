import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.pluginkit.android.library)
    alias(libs.plugins.pluginkit.quality)
    alias(libs.plugins.pluginkit.android.testing)
    alias(libs.plugins.pluginkit.android.network)
    alias(libs.plugins.pluginkit.coroutines)
    alias(libs.plugins.pluginkit.formatting)
    alias(libs.plugins.pluginkit.android.publishing)
}

android {
    namespace = "es.joshluq.pluginkit.mylibrary"

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
}

dependencies {

}
