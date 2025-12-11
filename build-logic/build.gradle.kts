plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "es.joshluq.kit.pluginkit"
version = "0.0.1-SNAPSHOT"

val agpVersion = "8.13.2"
val kotlinVersion = "2.2.21"
val detektVersion = "1.23.8"
val sonarVersion = "7.1.0.6387"
val koverVersion = "0.9.3"
val hiltVersion = "2.57.2"
val spotlessVersion = "8.1.0"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    //noinspection UseTomlInstead
    implementation("com.android.tools.build:gradle:$agpVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:$kotlinVersion")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${detektVersion}")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${sonarVersion}")
    implementation("org.jetbrains.kotlinx:kover-gradle-plugin:${koverVersion}")
    implementation("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${spotlessVersion}")
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "pluginkit.android.application"
            implementationClass = "es.joshluq.pluginkit.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidCompose") {
            id = "pluginkit.android.compose"
            implementationClass = "es.joshluq.pluginkit.buildlogic.AndroidComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "pluginkit.android.library"
            implementationClass = "es.joshluq.pluginkit.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("jvmLibrary") {
            id = "pluginkit.jvm.library"
            implementationClass = "es.joshluq.pluginkit.buildlogic.JvmLibraryConventionPlugin"
        }
        register("quality") {
            id = "pluginkit.quality"
            implementationClass = "es.joshluq.pluginkit.buildlogic.QualityConventionPlugin"
        }
        register("formatting") {
            id = "pluginkit.formatting"
            implementationClass = "es.joshluq.pluginkit.buildlogic.FormattingConventionPlugin"
        }
        register("androidTesting") {
            id = "pluginkit.android.testing"
            implementationClass = "es.joshluq.pluginkit.buildlogic.AndroidTestingConventionPlugin"
        }
        register("androidNetwork") {
            id = "pluginkit.android.network"
            implementationClass = "es.joshluq.pluginkit.buildlogic.AndroidNetworkConventionPlugin"
        }
        register("coroutines") {
            id = "pluginkit.coroutines"
            implementationClass = "es.joshluq.pluginkit.buildlogic.CoroutinesConventionPlugin"
        }
        register("androidHilt") {
            id = "pluginkit.android.hilt"
            implementationClass = "es.joshluq.pluginkit.buildlogic.HiltConventionPlugin"
        }
        register("androidNavigation") {
            id = "pluginkit.android.navigation"
            implementationClass = "es.joshluq.pluginkit.buildlogic.AndroidNavigationConventionPlugin"
        }
        register("androidFeature") {
            id = "pluginkit.android.feature"
            implementationClass = "es.joshluq.pluginkit.buildlogic.AndroidFeatureConventionPlugin"
        }
        register("androidPublishing") {
            id = "pluginkit.android.publishing"
            implementationClass = "es.joshluq.pluginkit.buildlogic.AndroidPublishingConventionPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}
