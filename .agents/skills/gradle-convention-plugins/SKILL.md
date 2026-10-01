---
name: gradle-convention-plugins
description: Expert guidelines for developing, refactoring, and maintaining Gradle Convention Plugins using Kotlin DSL in build-logic for Gradle 9.x and AGP 9.0 in PluginKit.
---

# Gradle Convention Plugins Specialist - PluginKit

This skill contains the authoritative guidelines, architecture patterns, and idioms for implementing and maintaining **Convention Plugins** inside the `build-logic` composite build in **PluginKit**.

---

## 1. `build-logic` Architecture

PluginKit uses a **Composite Build** included via `includeBuild("build-logic")` in `settings.gradle.kts`.

* **Source Location**: `build-logic/src/main/kotlin/es/joshluq/pluginkit/buildlogic/`
* **Plugin Registration**: `build-logic/build.gradle.kts` within the `gradlePlugin { plugins { ... } }` block.
* **Naming Conventions**: `pluginkit.<platform>.<feature>` (e.g., `pluginkit.android.library`, `pluginkit.quality`).
* **Shared Catalog**: `build-logic` consumes the centralized `gradle-catalog/libs.versions.toml` through its own `build-logic/settings.gradle.kts`.

---

## 2. Mandatory Rules for Gradle 9.x and AGP 9.0

### A. Mandatory Lazy Configuration API
Never use eager APIs that trigger early realization of tasks or extensions:
- **Correct**: `tasks.named("test") { ... }`, `tasks.withType<KotlinCompile>().configureEach { ... }`
- **Avoid**: `tasks.getByName(...)`, `tasks.all { ... }`

Use `Property<T>` and `Provider<T>` for all custom plugin extensions:
```kotlin
interface AndroidPublishingExtension {
    val repoUrl: Property<String>
    val artifactId: Property<String>
    val groupId: Property<String>
}
```

> [!IMPORTANT]
> Avoid `afterEvaluate { ... }`. Connect lazy extension properties directly to the underlying Gradle tasks or publishing models.

### B. Accessing the Version Catalog (`libs.versions.toml`)
To access catalog dependencies from a convention plugin in `build-logic`, use the provided extensions:

```kotlin
// Via BuildLogicExtensions.kt
val coreKtx = libs.findLibrary("androidx-core-ktx").get()
dependencies.add("implementation", coreKtx)

// Safe integer version with fallback
val compileSdk = getIntVersion("android-compileSdk", 37)
```

---

## 3. Composition Pattern (Composite Mega-Plugins)

High-level plugins (such as `pluginkit.android.feature`) compose baseline plugins without duplicating setup:

```kotlin
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply {
            apply("pluginkit.android.library")
            apply("pluginkit.android.hilt")
            apply("pluginkit.android.compose")
            apply("pluginkit.coroutines")
            apply("pluginkit.android.navigation")
        }
    }
}
```

---

## 4. Registering New Plugins

When adding a new convention plugin:
1. Create the Kotlin class: `es.joshluq.pluginkit.buildlogic.MyNewConventionPlugin : Plugin<Project>`.
2. Register it in `build-logic/build.gradle.kts`:
   ```kotlin
   gradlePlugin {
       plugins {
           register("myNewPlugin") {
               id = "pluginkit.my.new.plugin"
               implementationClass = "es.joshluq.pluginkit.buildlogic.MyNewConventionPlugin"
           }
       }
   }
   ```
3. Add the plugin definition to `gradle-catalog/libs.versions.toml` if external consumers need to reference it by alias.
4. Update the plugin table in `AGENTS.md` and `README.md`.
