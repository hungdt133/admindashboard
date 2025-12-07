package com.coffeeshop.models

data class Order(
    val id: String,
    val date: String,
    val customerName: String,
    val customerPhone: String,
    val totalPrice: String,
    val status: String // "Pending", "Confirmed", "Delivering", "Cancelled", "Uploaded"
)
