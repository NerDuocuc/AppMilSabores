package com.example.appmilsabores.domain.model

data class Category(
    val name: String,
    val iconRes: Int,
    // Optional representative image for the category (product-like image).
    // If present, UI will show this image instead of the small icon.
    val imageRes: Int? = null
)
