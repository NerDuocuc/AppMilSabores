package com.example.appmilsabores.data.source

import com.example.appmilsabores.model.ProductoDto
import com.example.appmilsabores.network.ApiClient
import io.mockk.coEvery
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class ProductRemoteDataSourceEdgeTest {

    @Before
    fun setup() {
        mockkObject(ApiClient)
    }

    @Test
    fun `fetchProducts gracefully skips malformed dtos`() = runTest {
        val good = ProductoDto(codigoProducto = "1", nombreProducto = "OK", precioProducto = 10, descripcionProducto = "", imagenProducto = null, imagenUrl = null, stock = 1, stockCritico = null, categoriaId = null, categoriaNombre = null)
        // malformed with nulls that might throw
        val bad = ProductoDto(codigoProducto = null, nombreProducto = null, precioProducto = null, descripcionProducto = null, imagenProducto = null, imagenUrl = null, stock = null, stockCritico = null, categoriaId = null, categoriaNombre = null)
        val mockService = io.mockk.mockk<com.example.appmilsabores.network.ApiService>(relaxed = true)
        coEvery { mockService.getProductos() } returns listOf(good, bad)

        val ds = ProductRemoteDataSourceImpl(apiService = mockService)
        val res = ds.fetchProducts()
        // Ensure at least the good product is present and mapped
        assertTrue(res.any { it.name == "OK" })
    }
}
