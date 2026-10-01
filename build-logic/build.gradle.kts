plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "es.joshluq.kit.pluginkit"
version = providers.gradleProperty("pluginVersion")
    .orElse(providers.gradleProperty("pluginKitVersion"))
    .getOrElse("2.0.0")



repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

val libsCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(dependencies.create("com.android.tools.build:gradle:${libsCatalog.findVersion("agp").get().requiredVersion}"))
    implementation(dependencies.create("org.jetbrains.kotlin:kotlin-gradle-plugin:${libsCatalog.findVersion("kotlin").get().requiredVersion}"))
    implementation(dependencies.create("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${libsCatalog.findVersion("kotlin").get().requiredVersion}"))
    implementation(dependencies.create("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${libsCatalog.findVersion("detektVersion").get().requiredVersion}"))
    implementation(dependencies.create("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${libsCatalog.findVersion("sonarVersion").get().requiredVersion}"))
    implementation(dependencies.create("org.jetbrains.kotlinx:kover-gradle-plugin:${libsCatalog.findVersion("koverVersion").get().requiredVersion}"))
    implementation(dependencies.create("com.google.dagger:hilt-android-gradle-plugin:${libsCatalog.findVersion("hilt").get().requiredVersion}"))
    implementation(dependencies.create("androidx.room:room-gradle-plugin:${libsCatalog.findVersion("room").get().requiredVersion}"))
    implementation(dependencies.create("com.diffplug.spotless:spotless-plugin-gradle:${libsCatalog.findVersion("spotless").get().requiredVersion}"))
    implementation(dependencies.create("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${libsCatalog.findVersion("ksp").get().requiredVersion}"))
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
        register("androidRoom") {
            id = "pluginkit.android.room"
            implementationClass = "es.joshluq.pluginkit.buildlogic.RoomConventionPlugin"
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
        register("jvmPublishing") {
            id = "pluginkit.jvm.publishing"
            implementationClass = "es.joshluq.pluginkit.buildlogic.JvmPublishingConventionPlugin"
        }
        register("androidWork") {
            id = "pluginkit.android.work"
            implementationClass = "es.joshluq.pluginkit.buildlogic.AndroidWorkConventionPlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY")}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
