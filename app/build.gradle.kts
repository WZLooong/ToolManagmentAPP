plugins {
    id("com.android.application") version "8.1.0"
    id("org.jetbrains.kotlin.android") version "1.9.20"
    id(toollibs.plugins.room) apply false
}

android {
    namespace "com.example.ToolManagmentAPP"
    compileSdk 35

    defaultConfig {
        applicationId "com.example.ToolManagmentAPP"
        minSdk 22
        targetSdk 35
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    implementation(toollibs.androidx.material3.android)
    testImplementation(toollibs.junit)
    implementation(toollibs.androidx.activity.compose)
    // 将 androidx.compose.material 依赖添加到 libs.versions.toml 后可通过 libs 引用，当前先移除硬编码依赖
    // implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.room:room-ktx:2.5.1")
    androidTestImplementation(toollibs.androidx.junit)
    androidTestImplementation(toollibs.androidx.espresso.core)
    // 移除重复的 Jetpack Compose 依赖
    //0427更新
}