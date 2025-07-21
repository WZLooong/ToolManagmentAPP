pluginManagement {
    repositories {
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") } // JetBrains Compose plugin repo
        maven { url = uri("https://mirrors.cloud.tencent.com/gradle-plugin/") }
        google()
        gradlePluginPortal()
        maven { url = uri("https://mirrors.cloud.tencent.com/repository/maven-public/") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Change to prefer settings repositories
    repositories {
        maven { url = uri("https://mirrors.cloud.tencent.com/repository/maven-public/") }
        google()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven { url = uri("https://mirrors.cloud.tencent.com/androidx/") }
        maven { url = uri("https://mirrors.cloud.tencent.com/google/") }
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