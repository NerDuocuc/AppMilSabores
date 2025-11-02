// En el archivo data/Usuario.kt
package com.example.appmilsabores.data

enum class RolUsuario {
    ADMINISTRADOR,
    COMPRADOR
}

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val rol: RolUsuario
)
    