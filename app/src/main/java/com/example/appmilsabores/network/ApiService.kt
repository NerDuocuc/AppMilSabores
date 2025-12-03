package com.example.appmilsabores.network

import com.example.appmilsabores.model.ProductoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.POST

// DTOs for auth
data class LoginRequest(val correo: String, val password: String)
data class LoginResponse(val token: String, val userId: String)

interface ApiService {
    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{codigo}")
    suspend fun getProducto(@Path("codigo") codigo: String): ProductoDto

    @PUT("api/productos/{codigo}")
    suspend fun updateProducto(@Path("codigo") codigo: String, @Body dto: ProductoDto): ProductoDto

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse
}
