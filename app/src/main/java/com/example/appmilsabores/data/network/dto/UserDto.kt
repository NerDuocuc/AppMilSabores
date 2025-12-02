package com.example.appmilsabores.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("run") val run: String?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("apellidos") val apellidos: String?,
    @SerializedName("correo") val correo: String?,
    @SerializedName("tipo_usuario") val tipoUsuario: String?
)
