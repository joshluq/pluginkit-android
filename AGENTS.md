# Contexto del Proyecto: PluginKit - Convention Plugin

## Descripción General
**PluginKit** es un repositorio independiente diseñado como una colección de **Gradle Convention Plugins**. Su función principal es actuar como el "Master Dependency Management" para el ecosistema de aplicaciones de la organización. Centraliza la lógica de construcción, versiones de librerías y configuraciones de plugins en una única fuente de verdad.

## Objetivos Principales
1.  **Gestión Centralizada de Dependencias**: Unificar versiones (AndroidX, Kotlin, Retrofit, Compose, etc.) utilizando Gradle Version Catalogs.
2.  **Estandarización**: Asegurar que todos los módulos (Apps y Librerías) compartan configuraciones críticas como `minSdk`, `targetSdk`, opciones de compilación de Kotlin y reglas de análisis estático.
3.  **Reducción de Boilerplate**: Eliminar la repetición de código en los archivos `build.gradle.kts` de cada módulo.
4.  **Facilidad de Mantenimiento**: Permitir actualizaciones transversales de dependencias modificando un solo punto en este repositorio.

## Arquitectura de Plugins
El proyecto proveerá los siguientes plugins de convención:

| ID del Plugin | Descripción |
| :--- | :--- |
| `pluginkit.android.application` | Configuración base para aplicaciones Android (SDK versions, plugins base). |
| `pluginkit.android.library` | Configuración para librerías Android (publicación, consumer proguard rules). |
| `pluginkit.android.compose` | Configuración específica para Jetpack Compose (compiler args, dependencias UI). |
| `pluginkit.jvm.library` | Configuración para módulos puros de Kotlin/Java. |
| `pluginkit.quality` | Herramientas de calidad de código (Detekt, Ktlint, Sonar). |

## Estructura del Proyecto
*   **`build-logic`**: Módulo incluido que contiene el código fuente de los plugins (Composite Build).
*   **`gradle/libs.versions.toml`**: Catálogo de versiones centralizado.
*   **`app`**: Módulo de aplicación de ejemplo para probar y validar los plugins en tiempo real.

## Tecnologías
*   Gradle Kotlin DSL
*   Gradle Version Catalogs (TOML)
*   Composite Builds
