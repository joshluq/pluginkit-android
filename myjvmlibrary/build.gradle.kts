plugins {
    alias(libs.plugins.pluginkit.jvm.library)
    alias(libs.plugins.pluginkit.jvm.publishing)
    alias(libs.plugins.pluginkit.quality)
    alias(libs.plugins.pluginkit.formatting)
}

group = "es.joshluq.pluginkit"
version = "1.0.0"

jvmPublishing {
    groupId = "es.joshluq.pluginkit"
    artifactId = "myjvmlibrary"
    version = "1.0.0"
    pomName = "My JVM Library"
    pomDescription = "Sample pure Kotlin JVM library"
}
