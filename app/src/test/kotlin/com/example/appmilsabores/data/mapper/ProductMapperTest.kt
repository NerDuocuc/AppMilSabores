package com.example.appmilsabores.data.mapper

import com.example.appmilsabores.data.local.entity.ProductEntity
import com.example.appmilsabores.domain.model.Product
import org.junit.Assert.*
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `toDomain maps fields including codigo and stock`() {
        val entity = ProductEntity(
            id = 123,
            codigo = "P-001",
            name = "Torta",
            price = 100.0,
            oldPrice = null,
            rating = 0f,
            reviews = 0,
            imageRes = 0,
            imageUrl = null,
            stock = 7,
            category = "tortas",
            description = "delicious"
        )

        val domain = ProductMapper.toDomain(entity)

        assertEquals(123, domain.id)
        assertEquals("P-001", domain.codigo)
        assertEquals(7, domain.stock)
        assertEquals("tortas", domain.category)
    }

    @Test
    fun `toEntity preserves codigo from domain`() {
        val domain = Product(
            id = 42,
            name = "Pan",
            price = 10.0,
            rating = 0f,
            reviews = 0,
            imageRes = 0,
            imageUrl = null,
            stock = 5,
            category = "panes",
            description = "",
            codigo = "PAN42"
        )

        val entity = ProductMapper.toEntity(domain)
        assertEquals("PAN42", entity.codigo)
        assertEquals(5, entity.stock)
    }
}
