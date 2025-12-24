plugins {
    `version-catalog`
    `maven-publish`
}

group = "es.joshluq.kit"
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

            pom {
                name.set("Shared Gradle Catalog")
                url.set("https://github.com/${System.getenv("GITHUB_REPOSITORY")}")
                scm {
                    connection.set("scm:git:github.com/${System.getenv("GITHUB_REPOSITORY")}.git")
                    developerConnection.set("scm:git:ssh://github.com/${System.getenv("GITHUB_REPOSITORY")}.git")
                    url.set("https://github.com/${System.getenv("GITHUB_REPOSITORY")}")
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY")}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
