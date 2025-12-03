import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.pluginkit.android.application)
    alias(libs.plugins.pluginkit.android.compose)
    alias(libs.plugins.pluginkit.quality)
    alias(libs.plugins.pluginkit.android.testing)
    alias(libs.plugins.pluginkit.android.hilt)
    alias(libs.plugins.pluginkit.android.navigation)
    alias(libs.plugins.pluginkit.coroutines)
    alias(libs.plugins.pluginkit.formatting)
}

pluginkitQuality {
    sonarHost = "https://your-sonar-instance.com"
    sonarProjectKey = "your-project-key"
    sonarToken = System.getenv("SONAR_TOKEN")
    sonarOrganization = "your-organization"
}

android {
    namespace = "es.joshluq.pluginkit"

    defaultConfig {
        applicationId = "es.joshluq.pluginkit"
        versionCode = 1
        versionName = "1.0"
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
}

dependencies {
    implementation(project(":mylibrary"))
}
