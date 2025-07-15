// Top-level build file where you can add configuration options common to all sub-projects/modules.
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.android.application") version (libs.versions.agp.get()) apply false
    id("org.jetbrains.kotlin.android") version (libs.versions.kotlin.get()) apply false
    id("androidx.room.room-gradle-plugin") version (libs.versions.androidxRoom.get()) apply false
    id("androidx.compose.compiler") version (libs.versions.ui.get()) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
// Ensure the plugin definitions in libs.versions.toml are correct
// Based on the current libs.versions.toml, the references should work as is
