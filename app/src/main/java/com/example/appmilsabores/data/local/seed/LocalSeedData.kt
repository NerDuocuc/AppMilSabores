package com.example.appmilsabores.data.local.seed

import com.example.appmilsabores.R
import com.example.appmilsabores.data.local.entity.ProductEntity
import com.example.appmilsabores.data.local.entity.UserEntity
import com.example.appmilsabores.domain.model.ProductReview

object LocalSeedData {

    val defaultProducts = listOf(
        // --- Tortas Especiales ---
        ProductEntity(
            id = 1,
            name = "Torta Especial de Cumpleaños",
            price = 35990.0,
            oldPrice = 39990.0,
            rating = 4.9f,
            reviews = 88,
            imageRes = R.drawable.torta_especial_cumpleanos, // <- TU IMAGEN
            category = "Tortas Especiales",
            description = "Una torta festiva y personalizable, con bizcocho de vainilla, relleno de crema y frutas de estación. Perfecta para celebrar."
        ),
        ProductEntity(
            id = 2,
            name = "Torta Especial de Boda",
            price = 89990.0,
            oldPrice = null,
            rating = 5.0f,
            reviews = 45,
            imageRes = R.drawable.torta_especial_boda, // <- TU IMAGEN
            category = "Tortas Especiales",
            description = "Elegancia y sabor en una torta de varios pisos. Sabores a elección, cubierta de fondant artesanal y detalles florales."
        ),

        // --- Tortas Tradicionales ---
        ProductEntity(
            id = 3,
            name = "Torta Circular de Vainilla",
            price = 22990.0,
            oldPrice = null,
            rating = 4.7f,
            reviews = 112,
            imageRes = R.drawable.torta_circular_vainilla, // <- TU IMAGEN
            category = "Tortas Tradicionales",
            description = "Un clásico que nunca falla. Bizcocho de vainilla húmedo y esponjoso, relleno de manjar casero y cubierto con merengue."
        ),
        ProductEntity(
            id = 4,
            name = "Torta Circular de Manjar",
            price = 23990.0,
            oldPrice = 25990.0,
            rating = 4.8f,
            reviews = 130,
            imageRes = R.drawable.torta_circular_manjar, // <- TU IMAGEN
            category = "Tortas Tradicionales",
            description = "Para los amantes del manjar. Múltiples capas de bizcocho fino intercaladas con abundante manjar y nueces tostadas."
        ),
        ProductEntity(
            id = 5,
            name = "Torta Cuadrada de Chocolate",
            price = 25990.0,
            oldPrice = null,
            rating = 4.9f,
            reviews = 155,
            imageRes = R.drawable.torta_cuadrada_chocolate, // <- TU IMAGEN
            category = "Tortas Tradicionales",
            description = "Intensidad y textura. Bizcocho de chocolate 70% cacao, relleno de ganache y cubierto con trozos de chocolate belga."
        ),
        ProductEntity(
            id = 6,
            name = "Torta Cuadrada de Frutas",
            price = 24990.0,
            oldPrice = null,
            rating = 4.6f,
            reviews = 95,
            imageRes = R.drawable.torta_cuadrada_frutas, // <- TU IMAGEN
            category = "Tortas Tradicionales",
            description = "Frescura y color. Bizcocho de yogur natural cubierto con crema pastelera y una selección de frutas frescas de la temporada."
        ),

        // --- Postres y Dulces ---
        ProductEntity(
            id = 7,
            name = "Postre de Tiramisú Clásico",
            price = 5990.0,
            oldPrice = 6490.0,
            rating = 4.9f,
            reviews = 210,
            imageRes = R.drawable.tiramisu, // <- TU IMAGEN (principal)
            category = "Postres y Dulces",
            description = "Capas de galletas de champaña bañadas en café de grano y licor, intercaladas con una suave crema de queso mascarpone."
        ),
        ProductEntity(
            id = 8,
            name = "Tiramisú para Compartir (Formato 2)",
            price = 15990.0,
            oldPrice = null,
            rating = 4.8f,
            reviews = 90,
            imageRes = R.drawable.tiramisu_2, // <- TU IMAGEN (alternativa)
            category = "Postres y Dulces",
            description = "El mismo sabor clásico de nuestro tiramisú, en un formato más grande ideal para compartir en una ocasión especial."
        ),
        ProductEntity(
            id = 9,
            name = "Mousse de Chocolate Intenso",
            price = 4990.0,
            oldPrice = null,
            rating = 4.8f,
            reviews = 180,
            imageRes = R.drawable.mousse_chocolate, // <- TU IMAGEN
            category = "Postres y Dulces",
            description = "Una mousse aireada y potente, hecha con chocolate de origen y un toque de sal marina para realzar su sabor."
        ),
        ProductEntity(
            id = 10,
            name = "Brownie de Chocolate",
            price = 2990.0,
            oldPrice = null,
            rating = 4.7f,
            reviews = 250,
            imageRes = R.drawable.brownie, // <- TU IMAGEN
            category = "Postres y Dulces",
            description = "Húmedo por dentro, con una capa crujiente por fuera y lleno de trozos de nueces y chocolate. Un clásico irresistible."
        ),
        ProductEntity(
            id = 11,
            name = "Brownie Doble Chocolate (Formato 2)",
            price = 3290.0,
            oldPrice = 3490.0,
            rating = 4.8f,
            reviews = 190,
            imageRes = R.drawable.brownie_2, // <- TU IMAGEN
            category = "Postres y Dulces",
            description = "Nuestra receta de brownie elevada al siguiente nivel, con doble carga de chocolate semi-amargo y una textura extra fudgy."
        ),

        // --- Opciones Saludables / Especiales ---
        ProductEntity(
            id = 12,
            name = "Torta Sin Azúcar de Naranja",
            price = 26990.0,
            oldPrice = null,
            rating = 4.5f,
            reviews = 65,
            imageRes = R.drawable.torta_sin_azucar_naranja, // <- TU IMAGEN
            category = "Opciones Saludables",
            description = "Bizcocho húmedo de naranja endulzado naturalmente, ideal para quienes buscan una opción deliciosa y sin azúcar añadida."
        ),
        ProductEntity(
            id = 13,
            name = "Brownie Sin Gluten",
            price = 3490.0,
            oldPrice = null,
            rating = 4.6f,
            reviews = 80,
            imageRes = R.drawable.brownie_sin_gluten, // <- TU IMAGEN
            category = "Opciones Saludables",
            description = "El mismo sabor intenso de nuestro brownie, pero elaborado con una mezcla de harinas sin gluten para que todos puedan disfrutarlo."
        ),
        ProductEntity(
            id = 14,
            name = "Pan de Molde Sin Gluten",
            price = 4990.0,
            oldPrice = null,
            rating = 4.4f,
            reviews = 55,
            imageRes = R.drawable.pan_sin_gluten, // <- TU IMAGEN
            category = "Opciones Saludables",
            description = "Pan artesanal sin gluten, de miga suave y corteza dorada. Perfecto para sándwiches o tostadas."
        ),
        ProductEntity(
            id = 15,
            name = "Pastel Vegano de Chocolate",
            price = 4990.0,
            oldPrice = null,
            rating = 4.7f,
            reviews = 92,
            imageRes = R.drawable.vegana_chocolate, // <- TU IMAGEN
            category = "Opciones Saludables",
            description = "Cremoso pastel de chocolate hecho a base de palta y cacao, endulzado con sirope de agave. 100% origen vegetal."
        ),

        // --- Pastelería Chilena ---
        ProductEntity(
            id = 16,
            name = "Empanada de Manzana",
            price = 2490.0,
            oldPrice = null,
            rating = 4.8f,
            reviews = 140,
            imageRes = R.drawable.empanada_manzana, // <- TU IMAGEN
            category = "Pastelería Chilena",
            description = "Masa horneada rellena de compota de manzana casera, con un toque de canela y pasas. Un clásico del sur de Chile."
        ),
        ProductEntity(
            id = 17,
            name = "Tarta de Santiago",
            price = 18990.0,
            oldPrice = null,
            rating = 4.9f,
            reviews = 105,
            imageRes = R.drawable.tarta_santiago, // <- TU IMAGEN
            category = "Pastelería Chilena",
            description = "Exquisita tarta de almendras molidas, de origen gallego pero adoptada en la repostería chilena. Densa, húmeda y sin harina de trigo."
        )
    )


    private fun seedUser(
        id: Long,
        run: String,
        nombre: String,
        apellidos: String,
        correo: String,
        perfil: String,
        fechaNacimiento: String?,
        region: String,
        comuna: String,
        direccion: String,
        descuentoVitalicio: Boolean,
        referral: String? = null,
        isSystem: Boolean = false
    ): UserEntity {
        val cleanedFirstName = nombre.trim()
        val cleanedLastName = apellidos.trim()
        val fullName = listOf(cleanedFirstName, cleanedLastName)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { cleanedFirstName }
        val normalizedEmail = correo.trim().lowercase()
        val roleIsAdmin = perfil.equals("Administrador", ignoreCase = true)
        val placeholderAvatar = R.drawable.avatar_placeholder
        return UserEntity(
            id = id,
            run = run.trim().ifBlank { null },
            fullName = fullName,
            firstName = cleanedFirstName.ifBlank { null },
            lastName = cleanedLastName.ifBlank { null },
            email = normalizedEmail,
            passwordHash = null,
            avatarRes = placeholderAvatar,
            profileRole = perfil,
            birthDate = fechaNacimiento?.takeIf { it.isNotBlank() },
            region = region.trim().ifBlank { null },
            comuna = comuna.trim().ifBlank { null },
            address = direccion.trim().ifBlank { null },
            referralCode = referral?.trim().takeUnless { it.isNullOrBlank() },
            hasLifetimeDiscount = descuentoVitalicio,
            isSuperAdmin = isSystem || roleIsAdmin,
            isSystem = isSystem
        )
    }

    val seededUsers: List<UserEntity> = listOf(
        seedUser(
            id = 1,
            run = "000000000",
            nombre = "System",
            apellidos = "",
            correo = "system@milsabores.local",
            perfil = "Administrador",
            fechaNacimiento = null,
            region = "",
            comuna = "",
            direccion = "",
            descuentoVitalicio = false,
            isSystem = true
        ),
        seedUser(
            id = 2,
            run = "190110222",
            nombre = "Felipe",
            apellidos = "Ahumada Silva",
            correo = "felipe@duoc.cl",
            perfil = "Cliente",
            fechaNacimiento = "1994-05-12",
            region = "Biobío",
            comuna = "Concepción",
            direccion = "Av. Los Carrera 123",
            descuentoVitalicio = true
        ),
        seedUser(
            id = 3,
            run = "123456785",
            nombre = "Ana",
            apellidos = "Pérez Gómez",
            correo = "ana@gmail.com",
            perfil = "Vendedor",
            fechaNacimiento = "1988-11-03",
            region = "RM",
            comuna = "Santiago",
            direccion = "Av. Providencia 456",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 4,
            run = "876543214",
            nombre = "Carlos",
            apellidos = "Muñoz Torres",
            correo = "carlos@profesor.duoc.cl",
            perfil = "Administrador",
            fechaNacimiento = "1982-07-21",
            region = "Valparaíso",
            comuna = "Viña del Mar",
            direccion = "Calle Marina 789",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 5,
            run = "111111111",
            nombre = "María",
            apellidos = "López Fernández",
            correo = "maria@duoc.cl",
            perfil = "Cliente",
            fechaNacimiento = "1996-02-17",
            region = "RM",
            comuna = "Las Condes",
            direccion = "Camino El Alba 321",
            descuentoVitalicio = true
        ),
        seedUser(
            id = 6,
            run = "222222222",
            nombre = "Javier",
            apellidos = "Rojas Díaz",
            correo = "jrojas@gmail.com",
            perfil = "Vendedor",
            fechaNacimiento = "1990-09-30",
            region = "Biobío",
            comuna = "Talcahuano",
            direccion = "Calle Colón 987",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 7,
            run = "333333333",
            nombre = "Constanza",
            apellidos = "Soto Herrera",
            correo = "constanza@profesor.duoc.cl",
            perfil = "Administrador",
            fechaNacimiento = "1985-12-05",
            region = "Valparaíso",
            comuna = "Quilpué",
            direccion = "Av. Los Carrera 147",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 8,
            run = "444444444",
            nombre = "Diego",
            apellidos = "García Morales",
            correo = "diegogm@gmail.com",
            perfil = "Cliente",
            fechaNacimiento = "2001-04-09",
            region = "RM",
            comuna = "Maipú",
            direccion = "Av. Pajaritos 654",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 9,
            run = "555555555",
            nombre = "Valentina",
            apellidos = "Ramírez Castro",
            correo = "valen@duoc.cl",
            perfil = "Vendedor",
            fechaNacimiento = "1993-08-14",
            region = "Valparaíso",
            comuna = "Villa Alemana",
            direccion = "Calle Central 852",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 10,
            run = "666666666",
            nombre = "Francisco",
            apellidos = "Fuentes Bravo",
            correo = "fran@profesor.duoc.cl",
            perfil = "Administrador",
            fechaNacimiento = "1984-03-28",
            region = "Biobío",
            comuna = "Chiguayante",
            direccion = "Pasaje Los Aromos 112",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 11,
            run = "777777777",
            nombre = "Camila",
            apellidos = "Martínez Vargas",
            correo = "camila@gmail.com",
            perfil = "Cliente",
            fechaNacimiento = "2000-06-22",
            region = "RM",
            comuna = "Providencia",
            direccion = "Av. Providencia 3456",
            descuentoVitalicio = false
        )
    )

    val superAdmin: UserEntity = seededUsers.firstOrNull { it.isSystem }
        ?: seededUsers.first { it.isSuperAdmin }

    val productReviews: Map<Int, List<ProductReview>> = mapOf(
        1 to listOf(
            ProductReview(
                id = "1_1",
                author = "Valentina R.",
                rating = 5f,
                comment = "Un clásico imperdible, siempre distinto cada vez que lo jugamos.",
                date = "03 nov 2025"
            ),
            ProductReview(
                id = "1_2",
                author = "Rodrigo C.",
                rating = 4.8f,
                comment = "Componentes de buena calidad y las expansiones lo hacen aún mejor.",
                date = "28 oct 2025"
            )
        ),
        4 to listOf(
            ProductReview(
                id = "4_1",
                author = "Carolina M.",
                rating = 4.9f,
                comment = "El control se siente muy sólido y la batería dura bastante.",
                date = "30 oct 2025"
            )
        ),
        5 to listOf(
            ProductReview(
                id = "5_1",
                author = "Jean P.",
                rating = 4.7f,
                comment = "Sonido envolvente real y micrófono claro, perfectos para raids.",
                date = "15 oct 2025"
            )
        ),
        7 to listOf(
            ProductReview(
                id = "7_1",
                author = "Andrea S.",
                rating = 5f,
                comment = "La velocidad de carga y los gatillos adaptativos son increíbles.",
                date = "01 nov 2025"
            )
        ),
        9 to listOf(
            ProductReview(
                id = "9_1",
                author = "Ian L.",
                rating = 4.8f,
                comment = "Corre todos mis juegos AAA en ultra sin problemas.",
                date = "22 oct 2025"
            )
        ),
        13 to listOf(
            ProductReview(
                id = "13_1",
                author = "Paula V.",
                rating = 4.9f,
                comment = "Sensor muy preciso, ideal para shooters competitivos.",
                date = "05 nov 2025"
            )
        ),
        19 to listOf(
            ProductReview(
                id = "19_1",
                author = "Martín F.",
                rating = 4.6f,
                comment = "Abriga harto y el estampado se mantiene después de varios lavados.",
                date = "12 sep 2025"
            )
        )
    )

}