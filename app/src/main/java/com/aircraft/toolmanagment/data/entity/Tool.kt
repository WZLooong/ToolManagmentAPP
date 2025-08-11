package com.aircraft.toolmanagment.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tools")
data class Tool(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val model: String = "",
    val specification: String = "",
    val quantity: Int = 0,
    val manufacturer: String = "",
    val purchaseDate: Long = 0,
    val storageLocation: String = "",
    val barcode: String = "",
    val status: String = "",
    val updatedAt: Long = 0
)