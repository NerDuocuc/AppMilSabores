package com.example.appmilsabores.data.source

import com.example.appmilsabores.data.mapper.ProductMapper
import com.example.appmilsabores.model.ProductoDto
import com.example.appmilsabores.network.ApiClient
import com.example.appmilsabores.domain.model.Product

class ProductRemoteDataSourceImpl : ProductRemoteDataSource {

    override suspend fun fetchProducts(): List<Product> {
        val dtos: List<ProductoDto> = ApiClient.service.getProductos()
        return dtos.mapNotNull { dto ->
            try {
                // Map ProductoDto -> Product domain
                val id = dto.codigoProducto?.toIntOrNull() ?: dto.hashCode()
                val price = dto.precioProducto?.toDouble() ?: 0.0
                val rawImage = dto.imagenProducto
                val imageUrl = rawImage?.let { path ->
                    // Backend returns paths like "/images/..." — prefix with emulator base URL for dev
                    if (path.startsWith("/")) {
                        "http://10.0.2.2:8080${path}"
                    } else {
                        path
                    }
                }
                Product(
                    id = id,
                    name = dto.nombreProducto.orEmpty(),
                    price = price,
                    oldPrice = null,
                    rating = 0f,
                    reviews = 0,
                    imageRes = 0,
                    imageUrl = imageUrl,
                    category = dto.categoriaNombre ?: "",
                    description = dto.descripcionProducto ?: ""
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}
