package com.aircraft.toolmanagment

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ToolManagementApplication : Application() {
    // Hilt will generate the necessary code for dependency injection
    // No additional implementation is required here
}