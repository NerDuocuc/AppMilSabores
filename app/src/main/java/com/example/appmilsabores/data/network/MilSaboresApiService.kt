package com.example.appmilsabores.data.network

import com.example.appmilsabores.data.network.dto.LoginRequestDto
import com.example.appmilsabores.data.network.dto.ProductDto
import com.example.appmilsabores.data.network.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MilSaboresApiService {

    @GET("api/productos")
    suspend fun getAllProducts(): Response<List<ProductDto>>

    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequestDto): Response<UserDto>
}
