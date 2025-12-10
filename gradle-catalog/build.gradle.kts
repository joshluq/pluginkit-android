plugins {
    `version-catalog`
    `maven-publish`
}

group = "es.joshluq.kit.pluginkit"
version = "0.0.1-SNAPSHOT"

catalog {
    versionCatalog {
        from(files("libs.versions.toml"))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])

            groupId = project.group.toString()
            artifactId = "catalog"
            version = project.version.toString()
        }
    }
    repositories {
        mavenLocal()
    }
}
