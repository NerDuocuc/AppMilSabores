package com.example.appmilsabores.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryInfo(
    val name: String,
    val icon: ImageVector,
    val sampleProducts: List<String>,
    // Optional representative image resource to show in category grid.
    val imageRes: Int? = null
)
