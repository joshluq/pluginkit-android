---
name: plugin-testing-and-validation
description: Runbooks and procedures for verifying, testing locally, and validating convention plugins in PluginKit, showcase modules, and local publishing with mavenLocal.
---

# Plugin Testing and Validation Specialist - PluginKit

This skill describes how to verify, debug, and validate convention plugins developed in **PluginKit** deterministically.

---

## 1. Test & Consumer Modules

The repository provides three modules that directly consume and validate the convention plugins:
* `showcase`: Android application module validating `pluginkit.android.application`, `pluginkit.android.compose`, Hilt, Room, and Navigation.
* `mylibrary`: Android library module validating `pluginkit.android.library`, quality tools, and `pluginkit.android.publishing`.
* `myjvmlibrary`: Pure JVM Kotlin module validating `pluginkit.jvm.library` and `pluginkit.jvm.publishing`.

---

## 2. Key Validation Commands

### Plugin Development Verification
Verify that `build-logic` compiles and passes all plugin validation tasks:
```bash
./gradlew :build-logic:check
```

### Static Analysis & Code Quality Checks
Run formatting and quality gates:
```bash
# Verify code formatting (Ktlint)
./gradlew spotlessCheck

# Apply automated formatting fixes
./gradlew spotlessApply

# Run Detekt static analysis
./gradlew detekt
```

### Build Consumer Modules
Test the compilation and packaging of consumer modules:
```bash
# Android App Showcase build
./gradlew :showcase:assembleDebug

# Android Library AAR packaging
./gradlew :mylibrary:assemble

# JVM Library JAR packaging & verification
./gradlew :myjvmlibrary:build
```

---

## 3. Local Maven Publication Validation (`mavenLocal`)

Before pushing code or triggering CI/CD pipelines, publish artifacts to your local Maven repository (`~/.m2/repository`):

```bash
# Publish all convention plugins to ~/.m2/repository
./gradlew :build-logic:publishToMavenLocal

# Publish Version Catalog to ~/.m2/repository
./gradlew :gradle-catalog:publishToMavenLocal

# Test publishing with a dynamic version override
./gradlew :build-logic:publishToMavenLocal :gradle-catalog:publishToMavenLocal -PpluginVersion="2.0.0-SNAPSHOT"
```

This verifies the generated POM files, metadata, and artifact bundles without touching remote GitHub Packages.
