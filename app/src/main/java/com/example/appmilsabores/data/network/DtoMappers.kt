package com.example.appmilsabores.data.network

import com.example.appmilsabores.R
import com.example.appmilsabores.data.network.dto.ProductDto
import com.example.appmilsabores.data.network.dto.UserDto
import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.model.User

fun ProductDto.toDomain(): Product {
    // id in domain is Int; use hashCode of codigoProducto as fallback
    val idInt = this.codigoProducto?.hashCode() ?: this.nombreProducto.hashCode()
    return Product(
        id = idInt,
        name = this.nombreProducto.orEmpty(),
        price = this.precioProducto ?: 0.0,
        oldPrice = null,
        rating = 0f,
        reviews = 0,
        imageRes = R.drawable.brownie_2,
        category = "",
        description = "",
        stock = this.stock ?: 0
    )
}

fun UserDto.toDomain(): User {
    val full = listOfNotNull(nombre, apellidos).joinToString(" ").ifBlank { nombre.orEmpty() }
    return User(
        id = run?.hashCode()?.toLong() ?: 0L,
        fullName = full,
        email = correo.orEmpty(),
        isSuperAdmin = (tipoUsuario ?: "").equals("Administrador", ignoreCase = true),
        avatarRes = R.drawable.profile_picture_male,
        run = run,
        firstName = nombre,
        lastName = apellidos,
        profileRole = tipoUsuario
    )
}
