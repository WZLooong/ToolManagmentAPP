package com.aircraft.toolmanagment.network

// Update user request model
data class UpdateUserRequest(
    val password: String? = null,
    val phone: String? = null,
    val email: String? = null
)