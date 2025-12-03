package com.example.appmilsabores.network

import com.example.appmilsabores.model.ProductoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{codigo}")
    suspend fun getProducto(@Path("codigo") codigo: String): ProductoDto
}
