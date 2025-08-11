package com.aircraft.toolmanagment.data.entity

data class UserLoginRequest(
    val identifier: String,
    val password: String
)