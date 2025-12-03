plugins {
    `kotlin-dsl`
}

group = "es.joshluq.pluginkit.buildlogic"

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.2.0")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
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
    }
}
