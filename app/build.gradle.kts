import org.gradle.api.JavaVersion
plugins {
    id("com.android.application") version "8.1.0"
    id("org.jetbrains.kotlin.android") version "1.9.20"
    id("org.jetbrains.compose") version "1.5.10"
    id("kotlin-kapt")
    id("androidx.room") version "2.6.1"
}

// 排除gradle:gradle依赖以阻止源码分发下载
configurations.all {
    exclude(group = "org.gradle", module = "gradle")
}

android {

    // Room数据库配置
    room {
        schemaDirectory("$projectDir/schemas")
    }
    namespace = "com.aircraft.toolmanagment"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "com.aircraft.toolmanagment"
        minSdk = 22
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }



}


dependencies {

    // 统一依赖格式，移除多余空格
    implementation(projectLibs.androidx.ui)
    implementation(projectLibs.androidx.ui.tooling.preview)
    implementation(projectLibs.androidx.core.ktx)
    implementation(projectLibs.androidx.ui.tooling)
    implementation(projectLibs.androidx.appcompat)
    implementation(projectLibs.material)
    implementation(projectLibs.androidx.activity)
    implementation(projectLibs.androidx.constraintlayout)
    implementation(projectLibs.androidx.room.runtime.android)
    implementation(projectLibs.androidx.room.ktx)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation(projectLibs.androidx.runtime)
    implementation(projectLibs.androidx.material3)
    testImplementation(projectLibs.classic.junit)
    implementation(projectLibs.androidx.activity.compose)

    implementation("androidx.compose.material:material:1.6.1")
    // 添加远程数据库连接所需依赖
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    // 添加MySQL JDBC驱动依赖
    implementation("mysql:mysql-connector-java:8.0.33")

    androidTestImplementation(projectLibs.androidx.junit)
    androidTestImplementation(projectLibs.androidx.espresso.core)

    // 移除重复的 Jetpack Compose 依赖
    //0427更新
}