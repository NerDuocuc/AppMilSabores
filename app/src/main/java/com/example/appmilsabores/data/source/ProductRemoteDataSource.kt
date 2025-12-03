package com.example.appmilsabores.data.source

import com.example.appmilsabores.domain.model.Product

interface ProductRemoteDataSource {
    suspend fun fetchProducts(): List<Product>
    suspend fun updateProductStock(codigo: String, newStock: Int): Product?
}
