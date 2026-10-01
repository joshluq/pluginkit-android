---
name: plugin-testing-and-validation
description: Procedimientos para verificar, probar localmente y validar plugins de convención en PluginKit, módulos de showcase y publicación local con mavenLocal.
---

# Plugin Testing and Validation Specialist - PluginKit

Este skill describe cómo verificar, depurar y validar los convention plugins desarrollados en **PluginKit** de manera local y determinista.

---

## 1. Módulos de Verificación

El repositorio cuenta con tres módulos de prueba/consumo directo de los plugins:
* `showcase`: Módulo de aplicación Android que valida `pluginkit.android.application`, `pluginkit.android.compose`, etc.
* `mylibrary`: Módulo de librería Android que valida `pluginkit.android.library`, Hilt, Room y publicación Android.
* `myjvmlibrary`: Módulo puro de Kotlin/Java que valida `pluginkit.jvm.library` y `pluginkit.jvm.publishing`.

---

## 2. Comandos Clave de Validación

### Compilación y Verificación de Plugins
Para validar que `build-logic` compila sin errores y cumple con las tareas de plugin development:
```bash
./gradlew :build-logic:check
```

### Comprobación Integral de Calidad
Ejecuta las herramientas estáticas (Detekt, Spotless, Linters):
```bash
# Validar formato
./gradlew spotlessCheck

# Aplicar formato automático
./gradlew spotlessApply

# Ejecutar análisis estático
./gradlew detekt
```

### Probar Compilación de los Módulos de Ejemplo
```bash
# Probar compilación del Showcase Android
./gradlew :showcase:assembleDebug

# Probar compilación de librerías
./gradlew :mylibrary:assemble
./gradlew :myjvmlibrary:build
```

---

## 3. Validación de Publicación Local (`mavenLocal`)

Antes de hacer push o disparar el workflow de GitHub Actions, puedes publicar los plugins y el catálogo en tu repositorio local Maven:

```bash
# Publicar todos los plugins a ~/.m2/repository
./gradlew :build-logic:publishToMavenLocal

# Publicar el Version Catalog a ~/.m2/repository
./gradlew :gradle-catalog:publishToMavenLocal
```

Esto permite verificar la estructura de los POM generados y los artefactos sin ensuciar GitHub Packages.
