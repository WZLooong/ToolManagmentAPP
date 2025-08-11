package com.aircraft.toolmanagment.data.entity

data class UserRegisterRequest(
    val phone: String?,
    val email: String?,
    val password: String,
    val name: String,
    val employeeId: String,
    val team: String,
    val role: String
)