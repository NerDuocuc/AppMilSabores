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
        Category("Tortas Especiales", R.drawable.ic_cake), // Ícono genérico, la imagen real está en el producto
        Category("Tortas Tradicionales", R.drawable.ic_pie),
        Category("Postres", R.drawable.ic_cupcake),
        Category("Saludables", R.drawable.ic_vegan),
        Category("Pastelería Chilena", R.drawable.ic_cookie)
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
        ProductSummary(
            id = 17,
            name = "Tarta de Santiago",
            price = "$18.990",
            imageRes = R.drawable.tarta_santiago
        ),
        ProductSummary(
            id = 15,
            name = "Pastel Vegano Choco",
            price = "$4.990",
            imageRes = R.drawable.vegana_chocolate
        ),
        ProductSummary(
            id = 5,
            name = "Torta Cuadrada Choco",
            price = "$25.990",
            imageRes = R.drawable.torta_cuadrada_chocolate
        )
    )
}
