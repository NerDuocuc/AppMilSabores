package com.example.appmilsabores.model

import com.google.gson.annotations.SerializedName

data class ProductoDto(
    @SerializedName("codigo_producto") val codigoProducto: String?,
    @SerializedName("nombre_producto") val nombreProducto: String?,
    @SerializedName("precio_producto") val precioProducto: Int?,
    @SerializedName("descripción_producto") val descripcionProducto: String?,
    @SerializedName("imagen_producto") val imagenProducto: String?,
    val stock: Int?,
    @SerializedName("stock_critico") val stockCritico: Int?,
    @SerializedName("categoria_id") val categoriaId: Int?,
    @SerializedName("nombre_categoria") val categoriaNombre: String?
)
