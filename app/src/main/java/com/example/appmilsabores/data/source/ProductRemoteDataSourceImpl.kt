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
                val imageUrl = when {
                    // Prefer explicit imagen_url returned by backend (may include leading '/').
                    dto.imagenUrl?.isNotBlank() == true -> {
                        val p = dto.imagenUrl!!
                        if (p.startsWith("/")) "http://10.0.2.2:8080${p}" else p
                    }
                    // Fallback to imagen_producto base name: build full URL pointing to images controller
                    dto.imagenProducto?.isNotBlank() == true -> {
                        val raw = dto.imagenProducto!!
                        if (raw.startsWith("/")) {
                            // if backend somehow returned a path with leading slash
                            "http://10.0.2.2:8080${raw}"
                        } else {
                            // build image lookup by base name; ImagesController will try known extensions
                            "http://10.0.2.2:8080/images/products/$raw"
                        }
                    }
                    else -> null
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
                    codigo = dto.codigoProducto,
                    category = dto.categoriaNombre ?: "",
                    description = dto.descripcionProducto ?: ""
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun updateProductStock(codigo: String, newStock: Int): Product? {
        return try {
            // Fetch current DTO from server, update stock and send PUT
            val current: ProductoDto = ApiClient.service.getProducto(codigo)
            val updated = current.copy(stock = newStock)
            val result: ProductoDto = ApiClient.service.updateProducto(codigo, updated)

            // Map returned DTO -> Product domain
            try {
                val id = result.codigoProducto?.toIntOrNull() ?: result.hashCode()
                val price = result.precioProducto?.toDouble() ?: 0.0
                val imageUrl = when {
                    result.imagenUrl?.isNotBlank() == true -> {
                        val p = result.imagenUrl!!
                        if (p.startsWith("/")) "http://10.0.2.2:8080${p}" else p
                    }
                    result.imagenProducto?.isNotBlank() == true -> {
                        val raw = result.imagenProducto!!
                        if (raw.startsWith("/")) {
                            "http://10.0.2.2:8080${raw}"
                        } else {
                            "http://10.0.2.2:8080/images/products/$raw"
                        }
                    }
                    else -> null
                }

                Product(
                    id = id,
                    name = result.nombreProducto.orEmpty(),
                    price = price,
                    oldPrice = null,
                    rating = 0f,
                    reviews = 0,
                    imageRes = 0,
                    imageUrl = imageUrl,
                    stock = result.stock ?: 0,
                    category = result.categoriaNombre ?: "",
                    description = result.descripcionProducto ?: "",
                    codigo = result.codigoProducto
                )
            } catch (e: Exception) {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
