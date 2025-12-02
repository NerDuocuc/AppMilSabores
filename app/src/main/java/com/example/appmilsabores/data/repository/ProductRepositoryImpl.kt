package com.example.appmilsabores.data.repository

import android.util.Log
import com.example.appmilsabores.data.network.MilSaboresApiService
import com.example.appmilsabores.data.network.NetworkModule
import com.example.appmilsabores.data.network.toDomain
import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.model.ProductFilters
import com.example.appmilsabores.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Single, clean implementation of ProductRepository backed by the network API.
 * Keeps behavior simple so tests can inject a mock MilSaboresApiService.
 */
class ProductRepositoryImpl(
    private val api: MilSaboresApiService = NetworkModule.apiService
) : ProductRepository {

    private val TAG = "ProductRepositoryImpl"

    override fun observeProducts(): Flow<List<Product>> = flowOf(emptyList())

    override suspend fun getProducts(filters: ProductFilters): List<Product> {
        return try {
            val response = api.getAllProducts()
            if (response.isSuccessful) {
                response.body()?.map { it.toDomain() } ?: emptyList()
            } else {
                Log.e(TAG, "Error API: ${'$'}{response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción API", e)
            emptyList()
        }
    }

    override suspend fun getProductsByCategory(categoryName: String, filters: ProductFilters): List<Product> {
        val all = getProducts(filters)
        return all.filter { it.category == categoryName }
    }

    override suspend fun getProductById(id: Int): Product? = getProducts().firstOrNull { it.id == id }

    override suspend fun searchProducts(query: String, filters: ProductFilters): List<Product> {
        val sanitized = query.trim().lowercase()
        if (sanitized.isEmpty()) return emptyList()
        return getProducts(filters).filter { p ->
            p.name.lowercase().contains(sanitized) || p.description.lowercase().contains(sanitized)
        }
    }

    override suspend fun updateProductStock(id: Int, stock: Int) {
        // No backend update implemented yet; log for visibility.
        Log.i(TAG, "updateProductStock called for id=${'$'}id stock=${'$'}stock (no-op)")
    }
}
