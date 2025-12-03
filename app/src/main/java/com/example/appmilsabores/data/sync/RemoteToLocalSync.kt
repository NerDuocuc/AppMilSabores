package com.example.appmilsabores.data.sync

import android.content.Context
import android.util.Log
import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.R
import com.example.appmilsabores.data.local.AppDatabase
import com.example.appmilsabores.data.local.entity.ProductEntity
import com.example.appmilsabores.model.ProductoDto
import com.example.appmilsabores.network.ApiClient

object RemoteToLocalSync {
    /**
     * Sync products from remote backend into local Room DB.
     * Returns true if sync succeeded, false otherwise.
     */
    suspend fun syncProducts(db: AppDatabase, context: Context): Boolean {
        try {
            val productos: List<ProductoDto> = ApiClient.service.getProductos()
            Log.d("Sync", "Fetched ${'$'}{productos.size} products from remote API")

            productos.forEach { dto ->
                Log.d("Sync", "Remote Producto - codigo:${'$'}{dto.codigoProducto} nombre:${'$'}{dto.nombreProducto} precio:${'$'}{dto.precioProducto} stock:${'$'}{dto.stock}")
            }

            val mapped = productos.map { dto ->
                val id = parseCodigoToId(dto.codigoProducto)
                ProductEntity(
                    id = id,
                    name = dto.nombreProducto ?: "",
                    price = dto.precioProducto?.toDouble() ?: 0.0,
                    oldPrice = null,
                    rating = 4.5f,
                    reviews = 0,
                    imageRes = 0,
                    imageUrl = dto.imagenProducto?.let { path ->
                        if (path.startsWith("/")) {
                            "http://10.0.2.2:8080${path}"
                        } else {
                            path
                        }
                    },
                    category = dto.categoriaNombre ?: "General",
                    description = dto.descripcionProducto
                )
            }

            // Clear local products first to remove any previous seed data, then upsert remote products
            db.productDao().clearProducts()
            db.productDao().upsertProducts(mapped)
            Log.d("Sync", "Synced ${'$'}{mapped.size} products from remote to local DB (replaced local seed)")
            return true
        } catch (e: Exception) {
            Log.e("Sync", "Failed to sync products", e)
            return false
        }
    }

    private fun parseCodigoToId(codigo: String?): Int {
        if (codigo == null) return generateFallbackId(codigo)
        return try {
            codigo.toInt()
        } catch (t: Throwable) {
            generateFallbackId(codigo)
        }
    }

    private fun generateFallbackId(codigo: String?): Int {
        val base = codigo?.hashCode() ?: System.currentTimeMillis().toInt()
        return kotlin.math.abs(base)
    }
}
