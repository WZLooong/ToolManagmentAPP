package com.aircraft.toolmanagment.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tool_transfers")
data class ToolTransfer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val toolId: Int = 0,
    val oldBorrowerId: Int = 0,
    val newBorrowerId: Int = 0,
    val transferTime: Long = 0,
    val transferReason: String = "",
    val location: String = ""
)