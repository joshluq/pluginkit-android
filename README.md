# PluginKit - Gradle Convention Plugins

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

**PluginKit** es una colección de **Gradle Convention Plugins** diseñados para actuar como una fuente única de verdad para la configuración de builds en un ecosistema de aplicaciones Android. Centraliza la lógica de construcción, las versiones de librerías y las configuraciones de plugins para estandarizar y simplificar la gestión de proyectos multi-módulo.

## ✨ Objetivos Principales

-   ✅ **Gestión Centralizada de Dependencias**: Unificar versiones de librerías (AndroidX, Kotlin, Compose, etc.) usando Gradle Version Catalogs.
-   ✅ **Estandarización de Builds**: Asegurar que todos los módulos compartan configuraciones críticas como `minSdk`, `targetSdk`, y opciones de compilación.
-   ✅ **Reducción de Boilerplate**: Eliminar la repetición de código en los archivos `build.gradle.kts` mediante la inyección automática de dependencias comunes.
-   ✅ **Mantenimiento Eficiente**: Permitir actualizaciones transversales de dependencias modificando un solo punto en este repositorio.

---

## 🔌 Plugins Disponibles

El proyecto provee los siguientes plugins de convención:

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


## 🚀 Modo de Uso

Gracias a la arquitectura de `build-logic` y los Convention Plugins, configurar un nuevo módulo es extremadamente simple.

### 1. Creando una Librería Publicable

Para una librería que necesita capacidades de red y que será publicada:

```kotlin
// mylibrary/build.gradle.kts

plugins {
    alias(libs.plugins.pluginkit.android.library)
    alias(libs.plugins.pluginkit.android.testing)
    alias(libs.plugins.pluginkit.quality)
    
    // Capacidades adicionales
    alias(libs.plugins.pluginkit.android.network)
    alias(libs.plugins.pluginkit.coroutines)

    // ¡Añade la capacidad de publicación!
    alias(libs.plugins.pluginkit.android.publishing)
}

android {
    namespace = "es.joshluq.pluginkit.mylibrary"
}
```
Para publicar, simplemente ejecuta `./gradlew :mylibrary:publish`.

### 2. Configurar Extensiones

Algunos plugins, como el de calidad, exponen una extensión para que puedas pasar parámetros de forma segura:

```kotlin
// showcase/build.gradle.kts

pluginkitQuality {
    sonarHost = "https://sonar.mycompany.com"
    sonarProjectKey = "my-project-key"
    // Lee el token desde variables de entorno para mayor seguridad
    sonarToken = System.getenv("SONAR_TOKEN")
}
```

---

## 🏗️ Estructura del Proyecto

-   **`build-logic/`**: Contiene el código fuente de todos los Convention Plugins.
-   **`gradle/libs.versions.toml`**: Es el catálogo de versiones de Gradle, nuestra única fuente de verdad para las dependencias.
-   **`showcase/`**: Módulo de aplicación Android que sirve como ejemplo de consumo de todos los plugins.
-   **`mylibrary/`**: Módulo de librería Android que demuestra el uso del plugin `pluginkit.android.library`.
-   **`config/`**: Contiene archivos de configuración compartidos, como el `detekt.yml`.

## 🛠️ Stack Tecnológico

-   Gradle Kotlin DSL
-   Gradle Version Catalogs (TOML)
-   Composite Builds
-   **Calidad de Código**: Detekt, SonarQube, Kover, Spotless (Ktlint)
-   **Testing**: JUnit, MockK, Espresso
-   **Infraestructura**: Hilt, Retrofit, Coroutines, Navigation, Serialization
-   **Publicación**: Maven Publish
