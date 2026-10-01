# PluginKit - Gradle Convention Plugins

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

**PluginKit** is a collection of **Gradle Convention Plugins** designed to act as a single source of truth for build configurations across an Android application ecosystem. It centralizes build logic, library versions, and plugin setups to standardize and streamline multi-module project management.

## ✨ Core Objectives

-   ✅ **Centralized Dependency Management**: Unify library versions (AndroidX, Kotlin, Compose, etc.) using Gradle Version Catalogs.
-   ✅ **Build Standardization**: Ensure all modules share baseline configurations including `minSdk`, `targetSdk`, and compilation options.
-   ✅ **Boilerplate Reduction**: Eliminate boilerplate across module `build.gradle.kts` files through automated dependency injection.
-   ✅ **Efficient Maintenance**: Enable organization-wide dependency updates by modifying a single point in this repository.

---

## 🔌 Available Convention Plugins

The project provides the following convention plugins:

| Plugin ID | Description | Automatic Injections / Applied Plugins |
| :--- | :--- | :--- |
| `pluginkit.android.application` | Base configuration for Android applications. | `androidx-core-ktx`, `lifecycle-runtime-ktx` |
| `pluginkit.android.library` | Base configuration for Android libraries. | `androidx-core-ktx`, `lifecycle-runtime-ktx` |
| `pluginkit.android.compose` | Jetpack Compose setup. | BOM, UI, Graphics, Tooling, Material3 |
| `pluginkit.android.testing` | Unified testing configuration. | JUnit, MockK, Espresso, Compose UI Test |
| `pluginkit.android.network` | Networking stack configuration. | Retrofit, OkHttp, Jackson Converter |
| `pluginkit.android.hilt` | Dependency Injection setup. | KSP, Hilt Android, Hilt Compiler |
| `pluginkit.android.navigation` | Type-safe navigation & serialization. | Navigation Compose, Hilt Nav, Kotlinx Serialization |
| `pluginkit.coroutines` | Asynchronous programming configuration. | Kotlinx Coroutines (Core & Android) |
| `pluginkit.android.feature` | **Composite Mega-Plugin** for Feature modules. | Library + Hilt + Compose + Coroutines + Navigation |
| `pluginkit.formatting` | Automated code formatting. | Spotless, Ktlint |
| `pluginkit.jvm.library` | Pure Kotlin/Java JVM modules. | Java 17 toolchain setup |
| `pluginkit.jvm.publishing` | Maven publication for pure JVM libraries. | Configurable via `jvmPublishing` extension |
| `pluginkit.quality` | Code quality & static analysis (Detekt, Sonar, Kover). | Configurable via `pluginkitQuality` extension |
| `pluginkit.android.publishing` | Maven publication for Android libraries. | Configurable via `androidPublishing` extension |
| `pluginkit.android.work` | WorkManager configuration. | WorkManager Runtime, Hilt Work, KSP |
| `pluginkit.android.room` | Local persistence with Room. | KSP, Room Runtime, Room KTX, Room Compiler |

> 💡 **Upcoming Plugins in Development**: Check our [**Plugin Evolution Roadmap**](docs/ROADMAP_PLUGINS.md) for planned additions like Baseline Profiles, Screenshot Testing, Compose Metrics, DataStore, and Security.

---

## 🚀 Usage Guide

Thanks to the `build-logic` architecture and Convention Plugins, configuring a new module is straightforward and concise.

### 1. Creating a Publishable Android Library

For an Android library module requiring networking and Maven publication:

```kotlin
// mylibrary/build.gradle.kts

plugins {
    alias(libs.plugins.pluginkit.android.library)
    alias(libs.plugins.pluginkit.android.testing)
    alias(libs.plugins.pluginkit.quality)
    
    // Additional capabilities
    alias(libs.plugins.pluginkit.android.network)
    alias(libs.plugins.pluginkit.coroutines)

    // Publication support!
    alias(libs.plugins.pluginkit.android.publishing)
}

configure<com.android.build.api.dsl.LibraryExtension> {
    namespace = "es.joshluq.pluginkit.mylibrary"
}

// Optional publication configuration
androidPublishing {
    repoUrl = "https://nexus.example.com/repository/maven-releases/"
    repoUser = System.getenv("REPO_USER")
    repoPassword = System.getenv("REPO_PASSWORD")
    artifactId = "my-library-name" // Optional, defaults to module name
}
```
To publish, run `./gradlew :mylibrary:publish`.

### 1b. Creating a Publishable Pure Kotlin JVM Library

For a pure JVM Kotlin module (no Android SDK dependencies):

```kotlin
// myjvmlibrary/build.gradle.kts

plugins {
    alias(libs.plugins.pluginkit.jvm.library)
    alias(libs.plugins.pluginkit.quality)
    alias(libs.plugins.pluginkit.formatting)

    // JVM publication support!
    alias(libs.plugins.pluginkit.jvm.publishing)
}

jvmPublishing {
    groupId = "es.joshluq.kit"
    artifactId = "my-jvm-library"
    version = "1.0.0"
    repoUrl = "https://nexus.example.com/repository/maven-releases/"
    pomName = "My JVM Library"
    pomDescription = "Pure Kotlin JVM library without Android dependencies"
}
```
To publish, run `./gradlew :myjvmlibrary:publish`.

### 2. Configuring Quality Extensions

Quality tools can be customized via the `pluginkitQuality` extension:

```kotlin
// showcase/build.gradle.kts

pluginkitQuality {
    sonarHost.set("https://sonar.mycompany.com")
    sonarProjectKey.set("my-project-key")
}
```

---

## 🚀 CI/CD & Automated Versioning Workflow

All convention plugins and the Version Catalog are versioned centrally in [`gradle.properties`](gradle.properties) through `pluginKitVersion=2.0.0`.

Publishing to **GitHub Packages** (`maven.pkg.github.com`) is completely automated via GitHub Actions:

| Event | Branch | Published Artifact | Behavior |
| :--- | :--- | :--- | :--- |
| **Pull Request** | `develop` or `main` | None | Validates `build-logic`, `gradle-catalog`, and sample consumer modules (`pr-checks.yml`). |
| **Merge / Push** | `develop` | `X.Y.Z-SNAPSHOT` | Automatically appends `-SNAPSHOT` to `libs.versions.toml` and publishes development builds (`publish-snapshots.yml`). |
| **Merge / Push** | `main` | `X.Y.Z` (Release) | Publishes the official clean version, creates the Git Tag `vX.Y.Z`, and generates the **GitHub Release** with changelog (`publish-release.yml`). |

> **Preparing a New Version**: Simply bump `pluginKitVersion=X.Y.Z` in `gradle.properties` on your working branch or PR. Merging to `main` handles publishing, tagging, and releases automatically.

---

## 📦 Local Development & Testing

To test plugin modifications locally before publishing:

1. Publish all plugins to your local Maven repository:
   ```bash
   ./gradlew :build-logic:publishToMavenLocal :gradle-catalog:publishToMavenLocal
   ```
2. In the consumer project, ensure `mavenLocal()` is included in `settings.gradle.kts` within `pluginManagement`.
3. Reference the published version (e.g., `2.0.0`).

---

## 🏗️ Project Structure

- **`build-logic/`**: Source code of all Gradle Convention Plugins (Composite Build).
- **`gradle-catalog/libs.versions.toml`**: Centralized Gradle Version Catalog (Single Source of Truth).
- **`showcase/`**: Sample Android application demonstrating full plugin integration.
- **`mylibrary/`**: Sample Android library validating `pluginkit.android.library` and `androidPublishing`.
- **`myjvmlibrary/`**: Sample JVM library validating `pluginkit.jvm.library` and `jvmPublishing`.
- **`config/`**: Shared static analysis configuration files (e.g., `detekt.yml`).

## 🛠️ Technology Stack

- **Gradle 9.x** & **Android Gradle Plugin 9.x**
- Gradle Kotlin DSL
- Gradle Version Catalogs (TOML)
- Composite Builds
- **Code Quality**: Detekt, SonarQube, Kover, Spotless (Ktlint)
- **Testing**: JUnit, MockK, Espresso
- **Architecture**: Hilt (KSP), Retrofit, Coroutines, Navigation Compose, Kotlinx Serialization
- **Publishing**: Maven Publish
