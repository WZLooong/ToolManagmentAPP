pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") } // JetBrains Compose plugin repo
        maven { url = uri("https://mirrors.cloud.tencent.com/gradle-plugin/") }
        maven { url = uri("https://mirrors.cloud.tencent.com/repository/maven-public/") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Change to prefer settings repositories
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven { url = uri("https://mirrors.cloud.tencent.com/androidx/") }
        maven { url = uri("https://mirrors.cloud.tencent.com/google/") }
        maven { url = uri("https://mirrors.cloud.tencent.com/repository/maven-public/") }
    }

    // Using a different name for the version catalog to avoid conflicts
    versionCatalogs {
        create("projectLibs") {
            from(files("gradle/libs.versions.toml"))
        }
    }

}

rootProject.name = "ToolManagementApp"
include(":app")