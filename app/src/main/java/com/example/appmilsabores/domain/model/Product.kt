package com.example.appmilsabores.domain.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val oldPrice: Double? = null,
    val rating: Float,
    val reviews: Int = 0,
    val imageRes: Int,
    val imageUrl: String? = null,
    val stock: Int = 0,
    val category: String,
    val description: String = "",
    // Server-side product code (may be non-numeric). Used when calling backend APIs.
    val codigo: String? = null
)