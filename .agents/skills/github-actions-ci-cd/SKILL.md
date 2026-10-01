---
name: github-actions-ci-cd
description: Directrices, mejores prácticas y solución de problemas para GitHub Actions y flujos de CI/CD en PluginKit, enfocado en compilación de Gradle, publicación de Convention Plugins y Version Catalogs a GitHub Packages Maven.
---

# GitHub Actions CI/CD Specialist - PluginKit

Este skill proporciona directrices autoritativas para diseñar, mantener y diagnosticar flujos de CI/CD en GitHub Actions para el repositorio **PluginKit**.

---

## 1. Contexto de Despliegue del Proyecto

PluginKit utiliza GitHub Actions para dos propósitos clave:
1. **Validación Continua (CI)**: Análisis estático, comprobación de formato y compilación de plugins y módulos de ejemplo (`showcase`, `mylibrary`, `myjvmlibrary`).
2. **Publicación Continua (CD)**: Publicación de los artefactos compilados a **GitHub Packages (`maven.pkg.github.com`)**:
   - `:build-logic:publish` -> Plugins Maven (`pluginkit.*`)
   - `:gradle-catalog:publish` -> Gradle Version Catalog (`es.joshluq.kit:catalog`)

---

## 2. Configuración Esencial para Publicación en GitHub Packages

### Permisos del Job (`permissions`)
Para que un workflow pueda publicar paquetes en GitHub Packages, debe declarar explícitamente permisos de escritura:

```yaml
permissions:
  contents: read
  packages: write
```

> [!IMPORTANT]
> Sin `packages: write`, Gradle fallará con un error `HTTP 401 Unauthorized` o `HTTP 403 Forbidden` al subir los artefactos Maven.

### Variables de Entorno y Autenticación
Los scripts de Gradle (`build-logic/build.gradle.kts` y `gradle-catalog/build.gradle.kts`) esperan las siguientes variables de entorno:
- `GITHUB_REPOSITORY`: Nombre en formato `owner/repo` (disponible por defecto en GitHub Actions).
- `GITHUB_ACTOR`: Usuario que ejecuta la acción (disponible por defecto o mapeado explícitamente).
- `GITHUB_TOKEN`: Secreto generado para el workflow (`${{ secrets.GITHUB_TOKEN }}`).

```yaml
- name: Publicar Plugins y Catálogo
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
    GITHUB_ACTOR: ${{ github.actor }}
    GITHUB_REPOSITORY: ${{ github.repository }}
  run: |
    chmod +x gradlew
    ./gradlew :build-logic:publish :gradle-catalog:publish --no-daemon --stacktrace
```

---

## 3. Estándar de Pasos para Gradle en GitHub Actions

Siempre utiliza las acciones oficiales optimizadas para rendimiento y caching:

```yaml
- name: Checkout del código
  uses: actions/checkout@v4

- name: Configurar JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: 'gradle'

- name: Configurar Gradle
  uses: gradle/actions/setup-gradle@v3
  with:
    cache-read-only: ${{ github.ref != 'refs/heads/main' && github.ref != 'refs/heads/develop' }}
```

---

## 4. Arquitectura de Workflows del Repositorio

El repositorio separa completamente la publicación continua de **Snapshots** y **Releases oficiales**, eliminando la necesidad de commits manuales de cambio de versión:

### A. Publicación de Snapshots (`publish-snapshots.yml`)
* **Disparador**: Push a rama `develop`.
* **Comportamiento**: Publica automáticamente con versión `X.Y.Z-SNAPSHOT` (definida en `gradle.properties`) a GitHub Packages.

### B. Publicación de Releases Oficiales (`publish-release.yml`)
* **Disparador**: Push o merge a la rama `main`.
* **Comportamiento**:
  1. Lee automáticamente `pluginKitVersion` de `gradle.properties` (ej. `2.0.0`).
  2. Publica en GitHub Packages con `-PpluginVersion="2.0.0"`.
  3. Crea automáticamente el Git Tag (ej. `v2.0.0`) y la **GitHub Release** oficial con notas y changelog de los PRs incluidos.


### C. Validación Continua en PRs (`pr-checks.yml`)
* **Disparador**: Pull Request hacia `main` o `develop`.
* **Comportamiento**: Ejecuta `./gradlew :build-logic:check :gradle-catalog:check :showcase:assembleDebug :mylibrary:assemble :myjvmlibrary:build`.


```yaml
name: PR Checks

on:
  pull_request:
    branches:
      - main
      - develop
    paths-ignore:
      - '**.md'
      - 'docs/**'

jobs:
  check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - uses: gradle/actions/setup-gradle@v3

      - name: Ejecutar Validaciones y Pruebas
        run: |
          chmod +x gradlew
          ./gradlew check --no-daemon
```

---

## 5. Diagnóstico de Errores Frecuentes

1. **`HTTP 401 / 403 Forbidden` al publicar**:
   - Revisa si el job tiene el bloque `permissions: packages: write`.
   - Verifica en los repositorios de GitHub que GitHub Packages esté habilitado con permisos de escritura para GitHub Actions (`Repo Settings -> Actions -> General -> Workflow permissions -> Read and write permissions`).
2. **`Permission Denied: ./gradlew`**:
   - Falta el flag ejecutable en el script del wrapper en Linux. Añade siempre `chmod +x gradlew` antes de invocarlo.
3. **Incompatibilidad de JVM**:
   - PluginKit utiliza Gradle 9.x y AGP 9.0; asegúrate de que el JDK configurado sea compatible (Java 17 o Java 21).
4. **URLs de Maven Packages en mayúsculas**:
   - GitHub Packages es sensible a mayúsculas/minúsculas en el endpoint (`owner/repo`). Asegúrate de que `GITHUB_REPOSITORY` coincida con la URL oficial del repositorio en GitHub.
