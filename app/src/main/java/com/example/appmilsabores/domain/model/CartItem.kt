package com.example.appmilsabores.domain.model

data class CartItem(
    val id: Int,
    val name: String,
    val price: Double,
    val imageRes: Int,
    val imageUrl: String? = null,
    val quantity: Int
)
