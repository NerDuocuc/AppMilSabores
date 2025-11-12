package com.example.appmilsabores.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import com.example.appmilsabores.domain.model.CategoryInfo
import com.example.appmilsabores.domain.repository.CategoryRepository
import com.example.appmilsabores.R
class CategoryRepositoryImpl : CategoryRepository {
    override fun getAllCategories() = listOf(
        CategoryInfo(
            "Tortas Cuadradas",
            Icons.Outlined.Cake,
            listOf("Torta Cuadrada de Chocolate", "Torta Cuadrada de Frutas"),
            imageRes = R.drawable.torta_cuadrada_chocolate
        ),
        CategoryInfo(
            "Tortas Circulares",
            Icons.Outlined.Cake,
            listOf("Torta Circular de Vainilla", "Torta Circular de Manjar"),
            imageRes = R.drawable.torta_circular_vainilla
        ),
        CategoryInfo(
            "Postres Individuales",
            Icons.Outlined.BakeryDining,
            listOf("Postre de Tiramisú", "Mousse de Chocolate", "Brownie de Chocolate"),
            imageRes = R.drawable.tiramisu
        ),
        CategoryInfo(
            "Productos sin Azúcar",
            Icons.Outlined.Eco,
            listOf("Torta Sin Azúcar de Naranja", "Cheesecake Sin Azúcar"),
            imageRes = R.drawable.torta_sin_azucar_naranja
        ),
        CategoryInfo(
            "Pastelería Tradicional",
            Icons.Outlined.Flag,
            listOf("Empanada de Manzana", "Tarta de Santiago"),
            imageRes = R.drawable.tarta_santiago
        ),
        CategoryInfo(
            "Productos sin Gluten",
            Icons.Outlined.Eco,
            listOf("Brownie Sin Gluten", "Pan Sin Gluten"),
            imageRes = R.drawable.brownie_sin_gluten
        ),
        CategoryInfo(
            "Productos Veganos",
            Icons.Outlined.Eco,
            listOf("Torta Vegana de Chocolate", "Galletas Veganas de Avena"),
            imageRes = R.drawable.vegana_chocolate
        ),
        CategoryInfo(
            "Tortas Especiales",
            Icons.Outlined.Celebration,
            listOf("Torta Especial de Cumpleaños", "Torta Especial de Boda"),
            imageRes = R.drawable.torta_especial_cumpleanos
        )
    )
}
