dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle-catalog/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
