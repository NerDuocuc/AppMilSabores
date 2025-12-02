package com.example.appmilsabores.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("codigo_producto") val codigoProducto: String?,
    @SerializedName("nombre_producto") val nombreProducto: String?,
    @SerializedName("precio_producto") val precioProducto: Double?,
    @SerializedName("stock") val stock: Int?,
    @SerializedName("imagen_url") val imagenUrl: String?
)
