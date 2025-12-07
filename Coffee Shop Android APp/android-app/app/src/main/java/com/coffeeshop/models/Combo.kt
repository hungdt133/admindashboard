package com.coffeeshop.models

data class Combo(
    val id: String,
    val title: String,
    val description: String,
    val items: List<String>,
    val price: String
)
