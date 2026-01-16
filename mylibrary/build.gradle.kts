import com.android.build.api.dsl.LibraryExtension
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

configure<LibraryExtension> {
    namespace = "es.joshluq.pluginkit.mylibrary"
}

dependencies {

}
