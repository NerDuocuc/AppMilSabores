// En el archivo data/Producto.kt
package com.example.appmilsabores.data

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String // Una URL o un identificador para la imagen
)
