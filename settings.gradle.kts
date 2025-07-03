pluginManagement {
    repositories {
        // 去重后的常用仓库（按优先级排序）
        google() // Google 官方仓库（含 Android 组件）
        gradlePluginPortal() // Gradle 插件门户
        mavenCentral() // Maven 中央仓库
        maven { url = uri("https://www.jitpack.io") }
        maven { url = uri("https://repo.maven.apache.org/maven2/") }
        // 可选：如果需要特定版本的插件，可保留其他仓库，但避免重复
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // 强制使用项目级仓库配置
    repositories {
        google() // 优先使用 Google 官方仓库解析 Android 依赖
        mavenCentral() // 补充 Maven 中央仓库的依赖
        maven { url = uri("https://www.jitpack.io") }
    }

    versionCatalogs {

        create("toollibs") {
            from(files("gradle/libs.versions.toml"))
           }

    }

}

rootProject.name = "ToolManagementApp"
include(":app")