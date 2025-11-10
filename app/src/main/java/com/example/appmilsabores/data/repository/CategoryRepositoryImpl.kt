package com.example.appmilsabores.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import com.example.appmilsabores.domain.model.CategoryInfo
import com.example.appmilsabores.domain.repository.CategoryRepository

class CategoryRepositoryImpl : CategoryRepository {
    override fun getAllCategories() = listOf(
        CategoryInfo("Tortas Especiales", Icons.Outlined.Celebration, listOf("Torta Especial de Cumpleaños", "Torta Especial de Boda")),
        CategoryInfo("Tortas Tradicionales", Icons.Outlined.Cake, listOf("Torta Circular de Vainilla", "Torta Cuadrada de Chocolate")),
        CategoryInfo("Postres y Dulces", Icons.Outlined.BakeryDining, listOf("Postre de Tiramisú", "Mousse de Chocolate", "Brownie de Chocolate")),
        CategoryInfo("Opciones Saludables", Icons.Outlined.Eco, listOf("Torta Sin Azúcar de Naranja", "Brownie Sin Gluten", "Pan de Molde Sin Gluten", "Pastel Vegano de Chocolate")),
        CategoryInfo("Pastelería Chilena", Icons.Outlined.Flag, listOf("Empanada de Manzana", "Tarta de Santiago"))
    )
}
