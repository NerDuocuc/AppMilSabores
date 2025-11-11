package com.example.appmilsabores.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import com.example.appmilsabores.domain.model.CategoryInfo
import com.example.appmilsabores.domain.repository.CategoryRepository
import com.example.appmilsabores.R
class CategoryRepositoryImpl : CategoryRepository {
    override fun getAllCategories() = listOf(
        CategoryInfo(
            "Tortas Especiales",
            Icons.Outlined.Celebration,
            listOf("Torta Especial de Cumpleaños", "Torta Especial de Boda"),
            imageRes = R.drawable.torta_especial_cumpleanos
        ),
        CategoryInfo(
            "Tortas Tradicionales",
            Icons.Outlined.Cake,
            listOf("Torta Circular de Vainilla", "Torta Cuadrada de Chocolate"),
            imageRes = R.drawable.torta_cuadrada_chocolate
        ),
        CategoryInfo(
            "Postres y Dulces",
            Icons.Outlined.BakeryDining,
            listOf("Postre de Tiramisú", "Mousse de Chocolate", "Brownie de Chocolate"),
            imageRes = R.drawable.tiramisu
        ),
        CategoryInfo(
            "Opciones Saludables",
            Icons.Outlined.Eco,
            listOf("Torta Sin Azúcar de Naranja", "Brownie Sin Gluten", "Pan de Molde Sin Gluten", "Pastel Vegano de Chocolate"),
            imageRes = R.drawable.torta_sin_azucar_naranja
        ),
        CategoryInfo(
            "Pastelería Chilena",
            Icons.Outlined.Flag,
            listOf("Empanada de Manzana", "Tarta de Santiago"),
            imageRes = R.drawable.tarta_santiago
        )
    )
}
