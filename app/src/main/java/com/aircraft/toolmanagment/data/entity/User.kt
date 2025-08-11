package com.aircraft.toolmanagment.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val password: String,
    val name: String,
    val employeeId: String,
    val phone: String? = null,
    val email: String? = null,
    val team: String,
    val role: String
) {
    // No-argument constructor for Room and Gson compatibility
    constructor() : this(0, "", "", "", "", null, null, "", "")
}