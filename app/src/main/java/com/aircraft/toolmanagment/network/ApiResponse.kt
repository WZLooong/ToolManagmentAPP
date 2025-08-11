package com.aircraft.toolmanagment.network

// API response data model
data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: UserData? = null
)