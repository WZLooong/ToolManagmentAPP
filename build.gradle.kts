// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version (toollibs.versions.agp.get()) apply false
    id("org.jetbrains.kotlin.android") version (toollibs.versions.kotlin.get()) apply false
}
// Ensure the plugin definitions in libs.versions.toml are correct
// Based on the current libs.versions.toml, the references should work as is

