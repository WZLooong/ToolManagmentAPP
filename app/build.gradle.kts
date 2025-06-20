// 注意：构建文件已更改，可能需要重新加载才能生效
plugins {
    id("com.android.application") version "8.1.0"
    // 请将此处替换为实际的 Kotlin 插件版本号
    id("org.jetbrains.kotlin.android") version "1.9.20"
    id("org.jetbrains.kotlin.android") version "1.9.20"
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.room:room-runtime:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")

    // 如果使用 Jetpack Compose
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation("androidx.compose.material3:material3:1.1.0")
    //0427更新
}