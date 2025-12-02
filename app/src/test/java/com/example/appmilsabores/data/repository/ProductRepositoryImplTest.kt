package com.example.appmilsabores.data.repository

import com.example.appmilsabores.data.network.MilSaboresApiService
import com.example.appmilsabores.data.network.dto.ProductDto
import com.example.appmilsabores.domain.model.ProductFilters
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ProductRepositoryImplTest {

    private lateinit var api: MilSaboresApiService
    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        repository = ProductRepositoryImpl(api = api)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getProducts_success() = runTest {
        // Arrange: prepare DTO list returned by API
        val dto = ProductDto(
            codigoProducto = "ABC123",
            nombreProducto = "Torta de Prueba",
            precioProducto = 19990.0,
            stock = 5,
            imagenUrl = null
        )
        coEvery { api.getAllProducts() } returns Response.success(listOf(dto))

        // Act
        val result = repository.getProducts(ProductFilters())

        // Assert: API called and mapping applied
        coVerify(exactly = 1) { api.getAllProducts() }
        assertEquals(1, result.size)
        assertEquals("Torta de Prueba", result[0].name)
        assertEquals(19990.0, result[0].price, 0.001)
    }

    @Test
    fun getProducts_apiError_returnsEmptyList() = runTest {
        // Arrange: simulate exception from API
        coEvery { api.getAllProducts() } throws RuntimeException("Network error")

        // Act
        val result = repository.getProducts(ProductFilters())

        // Assert: should not throw and should return empty list
        coVerify(exactly = 1) { api.getAllProducts() }
        assertEquals(emptyList<com.example.appmilsabores.domain.model.Product>(), result)
    }
}
