# Project Context: PluginKit - Gradle Convention Plugins

## Overview
**PluginKit** is an independent repository designed as a collection of **Gradle Convention Plugins**. Its primary goal is to serve as the "Master Dependency & Build Management" engine for the organization's application ecosystem. It centralizes build logic, library versions, and plugin configurations into a single source of truth.

## Main Objectives
1. **Centralized Dependency Management**: Unify library versions (AndroidX, Kotlin, Retrofit, Compose, etc.) using Gradle Version Catalogs (`libs.versions.toml`).
2. **Build Standardization**: Ensure all modules (applications and libraries) share baseline configurations such as `compileSdk`, `minSdk`, `targetSdk`, JVM toolchains, and static analysis quality gates.
3. **Boilerplate Reduction**: Eliminate boilerplate across module `build.gradle.kts` files through automatic injection of common dependencies and plugins.
4. **Maintainability**: Enable cross-cutting updates across all organizational apps by modifying a single point in this repository.

## Plugin Architecture
The project provides the following convention plugins:

| Plugin ID | Description | Automatic Injections / Applied Plugins |
| :--- | :--- | :--- |
| `pluginkit.android.application` | Base configuration for Android applications. | `com.android.application`, `androidx-core-ktx`, `lifecycle-runtime-ktx` |
| `pluginkit.android.library` | Base configuration for Android libraries. | `com.android.library`, `androidx-core-ktx`, `lifecycle-runtime-ktx` |
| `pluginkit.android.compose` | Jetpack Compose configuration. | BOM, UI, Graphics, Tooling, Material3 |
| `pluginkit.android.testing` | Unified test configuration. | JUnit, MockK, Espresso, Compose UI Test |
| `pluginkit.android.network` | Networking stack configuration. | Retrofit, OkHttp, Jackson Converter |
| `pluginkit.android.hilt` | Dependency injection configuration. | KSP, Hilt Android, Hilt Compiler |
| `pluginkit.android.room` | Local persistence configuration with Room. | KSP, Room Runtime, Room KTX, Room Compiler |
| `pluginkit.android.navigation` | Type-safe navigation & serialization. | Navigation Compose, Hilt Navigation, Kotlinx Serialization |
| `pluginkit.coroutines` | Asynchronous programming configuration. | Kotlinx Coroutines (Core & Android) |
| `pluginkit.android.feature` | **Composite Mega-Plugin** for feature modules. | Library + Hilt + Compose + Coroutines + Navigation |
| `pluginkit.formatting` | Code style & formatting. | Spotless, Ktlint |
| `pluginkit.jvm.library` | Pure Kotlin/Java JVM modules. | `org.jetbrains.kotlin.jvm`, Java 17 |
| `pluginkit.jvm.publishing` | Maven publication for pure JVM libraries. | Configurable via `jvmPublishing` extension |
| `pluginkit.quality` | Static analysis & code coverage. | Detekt, SonarQube, Kover (via `pluginkitQuality`) |
| `pluginkit.android.publishing` | Maven publication for Android libraries. | Configurable via `androidPublishing` extension |
| `pluginkit.android.work` | WorkManager configuration. | WorkManager Runtime, Hilt Work, KSP |

## Project Structure
* **`build-logic/`**: Included build (`includeBuild`) containing the convention plugins source code (Composite Build).
* **`gradle-catalog/`**: Subproject containing and publishing the centralized version catalog (`libs.versions.toml`).
* **`showcase/`**: Android application module showcasing the integration of all convention plugins.
* **`mylibrary/`**: Android library sample module validating `pluginkit.android.library` and `androidPublishing`.
* **`myjvmlibrary/`**: Pure JVM Kotlin module validating `pluginkit.jvm.library` and `jvmPublishing`.

## Core Tech Stack
* Gradle 9.x
* Android Gradle Plugin 9.x
* Gradle Kotlin DSL
* Gradle Version Catalogs (TOML)
* Composite Builds
* Detekt, SonarQube, Kover, MockK, Spotless (Ktlint)
* Retrofit, OkHttp, Hilt, Coroutines, KSP
* Navigation Compose, Kotlinx Serialization
* Maven Publish

## Specialized Workspace Skills (`.agents/skills/`)
The workspace includes dedicated skills to guide development, testing, and deployment:
1. [**`github-actions-ci-cd`**](file:///.agents/skills/github-actions-ci-cd/SKILL.md): Guidelines and workflows for continuous integration, verification, and deployment to GitHub Packages Maven.
2. [**`gradle-convention-plugins`**](file:///.agents/skills/gradle-convention-plugins/SKILL.md): Standards and idioms for Kotlin DSL convention plugins on Gradle 9+ and AGP 9.0 in `build-logic`.
3. [**`plugin-testing-and-validation`**](file:///.agents/skills/plugin-testing-and-validation/SKILL.md): Runbooks for local testing, showcase verification, and `mavenLocal` validation.

## Versioning & CI/CD Strategy
* **Centralized Version**: The base semantic version is configured in `gradle.properties` (`pluginKitVersion=X.Y.Z`). Never hardcode fixed versions in `build-logic/build.gradle.kts` or `gradle-catalog/build.gradle.kts`.
* **Continuous Development (Snapshots)**: Pushing or merging to `develop` automatically publishes `X.Y.Z-SNAPSHOT` artifacts to GitHub Packages via `publish-snapshots.yml`.
* **Official Releases**: Pushing or merging to `main` publishes the clean `X.Y.Z` release, generates the Git Tag `vX.Y.Z`, and creates the GitHub Release with changelog notes via `publish-release.yml`.
