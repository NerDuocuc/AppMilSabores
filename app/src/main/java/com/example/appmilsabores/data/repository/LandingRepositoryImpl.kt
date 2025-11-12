package com.example.appmilsabores.data.repository

import com.example.appmilsabores.R
import com.example.appmilsabores.domain.model.*
import com.example.appmilsabores.domain.repository.LandingRepository

class LandingRepositoryImpl : LandingRepository {

    override fun getPromotions() = listOf(
        Promotion("NUESTRAS ESPECIALIDADES", "Hecho a mano, con amor y tradición", R.drawable.vitrinareposteria), // <- TU IMAGEN
        Promotion("TORTAS PARA CADA OCASIÓN", "Cumpleaños, bodas y más", R.drawable.torta_especial_boda),
        Promotion("CONOCE A LA FUNDADORA", "La historia detrás de cada sabor", R.drawable.fundadora) // <- TU IMAGEN
    )

    override fun getCategories() = listOf(
        // Categories aligned with the web JSON
        Category("Tortas Cuadradas", R.drawable.ic_cake),
        Category("Tortas Circulares", R.drawable.ic_cake),
        Category("Postres Individuales", R.drawable.ic_cookie),
        Category("Productos sin Azúcar", R.drawable.ic_vegan),
        Category("Pastelería Tradicional", R.drawable.ic_cake),
        Category("Productos sin Gluten", R.drawable.ic_cookie),
        Category("Productos Veganos", R.drawable.ic_vegan),
        Category("Tortas Especiales", R.drawable.ic_especial)
    )

    override fun getFeaturedProducts() = listOf(
        ProductSummary(
            id = 7,
            name = "Postre de Tiramisú",
            price = "$5.990",
            imageRes = R.drawable.tiramisu
        ),
        ProductSummary(
            id = 1,
            name = "Torta de Cumpleaños",
            price = "$35.990",
            imageRes = R.drawable.torta_especial_cumpleanos
        ),
        ProductSummary(
            id = 12,
            name = "Torta Sin Azúcar",
            price = "$26.990",
            imageRes = R.drawable.torta_sin_azucar_naranja
        )
    )

    override fun getNewProducts() = listOf(
        // Mostrar los nuevos lanzamientos de la categoría "Opciones Saludables"
        ProductSummary(
            id = 12,
            name = "Torta Sin Azúcar",
            price = "$26.990",
            imageRes = R.drawable.torta_sin_azucar_naranja
        ),
        ProductSummary(
            id = 13,
            name = "Brownie Sin Gluten",
            price = "$3.490",
            imageRes = R.drawable.brownie_sin_gluten
        ),
        ProductSummary(
            id = 15,
            name = "Pastel Vegano Choco",
            price = "$4.990",
            imageRes = R.drawable.vegana_chocolate
        )
    )

    override fun getDessertProducts() = listOf(
        ProductSummary(
            id = 7,
            name = "Postre de Tiramisú",
            price = "$5.990",
            imageRes = R.drawable.tiramisu
        ),
        ProductSummary(
            id = 9,
            name = "Mousse de Chocolate Intenso",
            price = "$4.990",
            imageRes = R.drawable.mousse_chocolate
        ),
        ProductSummary(
            id = 10,
            name = "Brownie de Chocolate",
            price = "$2.990",
            imageRes = R.drawable.brownie
        )
    )
}
