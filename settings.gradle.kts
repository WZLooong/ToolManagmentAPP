pluginManagement {
    repositories {
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") // JetBrains Compose plugin repo
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // 添加 Maven 仓库用于依赖解析
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        }
    }

    versionCatalogs {
        create("toollibs") {
            from(files("gradle/libs.versions.toml"))
        }
    }

}

rootProject.name = "ToolManagementApp"
include(":app")