pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://repo.maven.apache.org/maven2")
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {

        create("toollibs") {
            from(files("gradle/libs.versions.toml"))
           }

    }

}

rootProject.name = "ToolManagementApp"
include(":app")