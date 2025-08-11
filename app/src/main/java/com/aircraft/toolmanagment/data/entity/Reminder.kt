package com.aircraft.toolmanagment.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int = 0,
    val toolId: Int = 0,
    val reminderType: String = "",
    val message: String = "",
    val sentTime: Long = 0
)