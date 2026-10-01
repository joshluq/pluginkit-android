---
name: gradle-convention-plugins
description: Guía experta para desarrollar, refactorizar y extender Gradle Convention Plugins con Kotlin DSL en build-logic, para Gradle 9.x y AGP 9.0 en PluginKit.
---

# Gradle Convention Plugins Specialist - PluginKit

Este skill contiene las directrices, arquitecturas y patrones autoritativos para implementar y mantener los **Convention Plugins** dentro del composite build `build-logic` de **PluginKit**.

---

## 1. Arquitectura de `build-logic`

PluginKit utiliza un **Composite Build** incluido mediante `includeBuild("build-logic")` en `settings.gradle.kts`.

* **Ubicación del código**: `build-logic/src/main/kotlin/es/joshluq/pluginkit/buildlogic/`
* **Registro de plugins**: `build-logic/build.gradle.kts` dentro del bloque `gradlePlugin { plugins { ... } }`.
* **Convención de IDs**: `pluginkit.<plataforma>.<feature>` (ej. `pluginkit.android.library`, `pluginkit.quality`).

---

## 2. Reglas Mandatorias para Gradle 9.x y AGP 9.0

### A. Lazy Configuration API Obligatoria
Nunca uses APIs ansiosas (eager APIs) que fuerzan la resolución temprana de tareas o extensiones:
- **Correcto**: `tasks.named("test") { ... }`, `tasks.withType<KotlinCompile>().configureEach { ... }`
- **Incorrecto**: `tasks.getByName(...)`, `tasks.all { ... }`

Usa siempre `Property<T>` y `Provider<T>` en las extensiones del plugin:
```kotlin
interface PluginKitQualityExtension {
    val enableDetekt: Property<Boolean>
    val enableSonar: Property<Boolean>
}
```

### B. Acceso al Version Catalog (`libs.versions.toml`)
Para consumir librerías o bundles definidos en el catálogo desde un plugin en `build-logic`:

```kotlin
val VersionCatalog = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

// Dependencias directas
val coroutines = libs.findLibrary("kotlinx-coroutines-core").get()
dependencies.add("implementation", coroutines)

// Versiones
val minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
```

---

## 3. Patrón de Composición (Mega-Plugins)

Los plugins de alto nivel (como `pluginkit.android.feature`) componen plugins base de forma modular sin duplicar lógica:

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

## 4. Registro de Nuevos Plugins

Al crear un nuevo plugin en Kotlin:
1. Crea la clase `es.joshluq.pluginkit.buildlogic.MiNuevoConventionPlugin` implementando `Plugin<Project>`.
2. Regístralo en `build-logic/build.gradle.kts`:
   ```kotlin
   gradlePlugin {
       plugins {
           register("miNuevoPlugin") {
               id = "pluginkit.mi.nuevo.plugin"
               implementationClass = "es.joshluq.pluginkit.buildlogic.MiNuevoConventionPlugin"
           }
       }
   }
   ```
3. Añade la configuración en el catálogo de versiones si aplica.
4. Actualiza la tabla de plugins en `AGENTS.md`.
