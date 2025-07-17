// Top-level build file where you can add configuration options common to all sub-projects/modules.
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.android.application") version (toollibs.versions.agp.get()) apply false
    id("org.jetbrains.kotlin.android") version (toollibs.versions.kotlin.get()) apply false

    id("androidx.compose.compiler") version (toollibs.versions.ui.get()) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
// Ensure the plugin definitions in libs.versions.toml are correct
// Based on the current libs.versions.toml, the references should work as is
