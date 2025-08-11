package com.aircraft.toolmanagment.network

// Add tool request model
data class AddToolRequest(
    val name: String,
    val model: String? = null,
    val specification: String? = null,
    val quantity: Int,
    val manufacturer: String? = null,
    val purchaseDate: String? = null,
    val storageLocation: String? = null,
    val barcode: String? = null,
    val status: String? = null
)