// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.androidx.compose) apply false

    // Ensure the plugin definitions in libs.versions.toml are correct
    // Based on the current libs.versions.toml, the references should work as is
}