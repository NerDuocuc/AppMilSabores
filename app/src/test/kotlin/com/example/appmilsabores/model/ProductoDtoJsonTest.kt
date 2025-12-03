package com.example.appmilsabores.model

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class ProductoDtoJsonTest {
    @Test
    fun `gson deserializes stock and imagen_url correctly`() {
        val json = """
        {
          "codigo_producto": "X1",
          "nombre_producto": "N",
          "precio_producto": 10,
          "descripción_producto": "d",
          "imagen_producto": "img",
          "imagen_url": "/images/products/img",
          "stock": 12,
          "stock_critico": 2,
          "categoria_id": 5,
          "nombre_categoria": "cat"
        }
        """

        val dto = Gson().fromJson(json, ProductoDto::class.java)
        assertEquals(12, dto.stock?.toInt())
        assertEquals("/images/products/img", dto.imagenUrl)
        assertEquals("X1", dto.codigoProducto)
    }
}
