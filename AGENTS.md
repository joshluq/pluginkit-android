# Contexto del Proyecto: PluginKit - Convention Plugin

## Descripción General
**PluginKit** es un repositorio independiente diseñado como una colección de **Gradle Convention Plugins**. Su función principal es actuar como el "Master Dependency Management" para el ecosistema de aplicaciones de la organización. Centraliza la lógica de construcción, versiones de librerías y configuraciones de plugins en una única fuente de verdad.

## Objetivos Principales
1.  **Gestión Centralizada de Dependencias**: Unificar versiones (AndroidX, Kotlin, Retrofit, Compose, etc.) utilizando Gradle Version Catalogs.
2.  **Estandarización**: Asegurar que todos los módulos (Apps y Librerías) compartan configuraciones críticas como `minSdk`, `targetSdk`, opciones de compilación de Kotlin y reglas de análisis estático.
3.  **Reducción de Boilerplate**: Eliminar la repetición de código en los archivos `build.gradle.kts` de cada módulo mediante la inyección automática de dependencias base.
4.  **Facilidad de Mantenimiento**: Permitir actualizaciones transversales modificando un solo punto en este repositorio.

## Arquitectura de Plugins
El proyecto proveerá los siguientes plugins de convención:

| ID del Plugin | Descripción | Inyección Automática |
| :--- | :--- | :--- |
| `pluginkit.android.application` | Configuración base para aplicaciones Android. | `androidx-core-ktx`, `lifecycle-runtime-ktx` |
| `pluginkit.android.library` | Configuración para librerías Android. | `androidx-core-ktx`, `lifecycle-runtime-ktx` |
| `pluginkit.android.compose` | Configuración específica para Jetpack Compose. | BOM, UI, Graphics, Tooling, Material3 |
| `pluginkit.android.testing` | Configuración unificada de pruebas. | JUnit, MockK, Espresso, Compose UI Test |
| `pluginkit.android.network` | Configuración para capa de red. | Retrofit, OkHttp, Jackson Converter |
| `pluginkit.android.hilt` | Configuración de Inyección de Dependencias. | Kapt, Hilt Android, Hilt Compiler |
| `pluginkit.android.navigation` | Configuración de Navegación y Serialización. | Navigation Compose, Hilt Nav, Kotlinx Serialization |
| `pluginkit.coroutines` | Configuración de programación asíncrona. | Kotlinx Coroutines (Core & Android) |
| `pluginkit.android.feature` | **Mega-Plugin** para módulos de Feature. | Library + Hilt + Compose + Coroutines + Navigation |
| `pluginkit.formatting` | Formateo de código automático. | Spotless, Ktlint |
| `pluginkit.jvm.library` | Configuración para módulos puros de Kotlin/Java. | - |
| `pluginkit.quality` | Herramientas de calidad de código (Detekt, Sonar, Kover). | Configurable vía extensión `pluginkitQuality` |
| `pluginkit.android.publishing` | Publicación de librerías a Nexus/Artifactory. | - |

## Estructura del Proyecto
*   **`build-logic`**: Módulo incluido que contiene el código fuente de los plugins (Composite Build).
*   **`gradle/libs.versions.toml`**: Catálogo de versiones centralizado.
*   **`showcase`**: (Antes `app`) Módulo de aplicación de ejemplo que consume los plugins y demuestra su integración.
*   **`mylibrary`**: Módulo de librería de ejemplo para validar la configuración de `pluginkit.android.library`.

## Tecnologías
*   Gradle Kotlin DSL
*   Gradle Version Catalogs (TOML)
*   Composite Builds
*   Detekt, SonarQube, Kover, MockK, Spotless (Ktlint)
*   Retrofit, OkHttp, Hilt, Coroutines
*   Navigation Compose, Kotlin Serialization
*   Maven Publish
