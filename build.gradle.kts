// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(toollibs.plugins.android.application) apply false
    alias(toollibs.plugins.kotlin.android) apply false
    alias(toollibs.plugins.room) apply false
    alias(toollibs.plugins.compose) apply false

    // Ensure the plugin definitions in libs.versions.toml are correct
    // Based on the current libs.versions.toml, the references should work as is
}