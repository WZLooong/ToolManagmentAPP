pluginManagement {
    repositories {
        // 去重后的常用仓库（按优先级排序）
        maven { url = uri("https://maven.aliyun.com/repository/public") } // 阿里云公共仓库（包含常用库）
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") } // 阿里云 Gradle 插件仓库
        google() // Google 官方仓库（含 Android 组件）
        mavenCentral() // Maven 中央仓库
        maven { url = uri("https://www.jitpack.io") } // JitPack 仓库（用于第三方 GitHub 项目）

        // 可选：如果需要特定版本的插件，可保留其他仓库，但避免重复
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // 强制使用项目级仓库配置
    repositories {
        google() // 优先使用 Google 官方仓库解析 Android 依赖
        mavenCentral() // 补充 Maven 中央仓库的依赖
    }

    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml")) // 仅调用一次 from（正确）
        }
    }
}

rootProject.name = "ToolManagementApp"
include(":app")