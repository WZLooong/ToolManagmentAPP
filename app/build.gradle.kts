import org.gradle.api.JavaVersion
plugins {
    id("com.android.application") version "8.1.0"
    id("org.jetbrains.kotlin.android") version "1.9.20"
    id("org.jetbrains.compose") version "1.5.10"
}

android {
    namespace = "com.example.ToolManagmentAPP"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "com.example.ToolManagmentAPP"
        minSdk = 22
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(toollibs.androidx.ui)
    implementation(toollibs.androidx.ui.tooling.preview)
    implementation(toollibs.androidx.core.ktx)
    implementation(toollibs.androidx.ui.tooling)
    implementation(toollibs.androidx.appcompat)
    implementation(toollibs.material)
    implementation(toollibs.androidx.activity)
    implementation(toollibs.androidx.constraintlayout)
    implementation(toollibs.androidx.room.runtime.android)
    implementation(toollibs.androidx.runtime)
    implementation(toollibs.androidx.material3)
    testImplementation(toollibs.classic.junit)
    implementation(toollibs.androidx.activity.compose)

    implementation("androidx.compose.material:material:1.6.1")
    implementation(toollibs.androidx.room.ktx)

    androidTestImplementation(toollibs.androidx.junit)
    androidTestImplementation(toollibs.androidx.espresso.core)

    // 移除重复的 Jetpack Compose 依赖
    //0427更新
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")

    implementation(composeBom)
    androidTestImplementation(composeBom)
}