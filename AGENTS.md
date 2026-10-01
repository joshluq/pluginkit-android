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
| `pluginkit.android.hilt` | Configuración de Inyección de Dependencias. | KSP, Hilt Android, Hilt Compiler |
| `pluginkit.android.room` | Configuración de Persistencia Local con Room. | KSP, Room Runtime, Room KTX, Room Compiler |
| `pluginkit.android.navigation` | Configuración de Navegación y Serialización. | Navigation Compose, Hilt Nav, Kotlinx Serialization |
| `pluginkit.coroutines` | Configuración de programación asíncrona. | Kotlinx Coroutines (Core & Android) |
| `pluginkit.android.feature` | **Mega-Plugin** para módulos de Feature. | Library + Hilt + Compose + Coroutines + Navigation |
| `pluginkit.formatting` | Formateo de código automático. | Spotless, Ktlint |
| `pluginkit.jvm.library` | Configuración para módulos puros de Kotlin/Java. | - |
| `pluginkit.jvm.publishing` | Publicación de librerías puras de Kotlin/Java a repositorios Maven. | Configurable vía extensión `jvmPublishing` |
| `pluginkit.quality` | Herramientas de calidad de código (Detekt, Sonar, Kover). | Configurable vía extensión `pluginkitQuality` |
| `pluginkit.android.publishing` | Publicación de librerías Android a repositorios Maven. | Configurable vía extensión `androidPublishing` |

## Estructura del Proyecto
*   **`build-logic`**: Módulo incluido que contiene el código fuente de los plugins (Composite Build).
*   **`gradle/libs.versions.toml`**: Catálogo de versiones centralizado.
*   **`showcase`**: (Antes `app`) Módulo de aplicación de ejemplo que consume los plugins y demuestra su integración.
*   **`mylibrary`**: Módulo de librería de ejemplo para validar la configuración de `pluginkit.android.library`.

## Tecnologías
*   Gradle 9.1
*   Android Gradle Plugin 9.0
*   Gradle Kotlin DSL
*   Gradle Version Catalogs (TOML)
*   Composite Builds
*   Detekt, SonarQube, Kover, MockK, Spotless (Ktlint)
*   Retrofit, OkHttp, Hilt, Coroutines, KSP
*   Navigation Compose, Kotlin Serialization
*   Maven Publish

## Skills Especializados del Workspace (`.agents/skills/`)
El repositorio cuenta con skills dedicados para guiar el desarrollo, testing y despliegue del proyecto:
1. [**`github-actions-ci-cd`**](file:///.agents/skills/github-actions-ci-cd/SKILL.md): Flujos de integración y despliegue continuo con GitHub Actions y publicación en GitHub Packages Maven.
2. [**`gradle-convention-plugins`**](file:///.agents/skills/gradle-convention-plugins/SKILL.md): Pautas y estándares de desarrollo de plugins de convención en Kotlin DSL para Gradle 9+ y AGP 9.0 en `build-logic`.
3. [**`plugin-testing-and-validation`**](file:///.agents/skills/plugin-testing-and-validation/SKILL.md): Guías de prueba local, validación de módulos de ejemplo (`showcase`, librerías) y `mavenLocal`.

## Estrategia de Versionado y Despliegue (CI/CD)
* **Versión centralizada**: La versión semántica base se define en `gradle.properties` (`pluginKitVersion=X.Y.Z`). Nunca hardcodear versiones fijas en `build-logic/build.gradle.kts` ni `gradle-catalog/build.gradle.kts`.
* **Desarrollo continuo**: Al hacer push/merge a `develop`, el workflow `publish-snapshots.yml` publica automáticamente artefactos `X.Y.Z-SNAPSHOT` en GitHub Packages.
* **Releases oficiales**: Al hacer push/merge a `main`, el workflow `publish-release.yml` publica la versión oficial limpia `X.Y.Z`, crea el Git Tag `vX.Y.Z` y genera la GitHub Release automáticamente.


