package com.example.appmilsabores.data.source

import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.model.ProductoDto
import com.example.appmilsabores.network.ApiClient
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProductRemoteDataSourceImplTest {

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(ApiClient)
    }

    @Test
    fun `fetchProducts maps imagen_url with leading slash to full url`() = runTest {
        val dto = ProductoDto(
            codigoProducto = "1",
            nombreProducto = "A",
            precioProducto = 100,
            descripcionProducto = "d",
            imagenProducto = null,
            imagenUrl = "/images/products/foo",
            stock = 3,
            stockCritico = null,
            categoriaId = null,
            categoriaNombre = "cat"
        )

        val mockService = io.mockk.mockk<com.example.appmilsabores.network.ApiService>(relaxed = true)
        coEvery { mockService.getProductos() } returns listOf(dto)

        val ds = ProductRemoteDataSourceImpl(apiService = mockService)
        val products = ds.fetchProducts()
        assertEquals(1, products.size)
        val p = products[0]
        assertTrue(p.imageUrl!!.startsWith("http://10.0.2.2:8080"))
        assertEquals(3, p.stock)
        assertEquals("1", p.codigo)
    }

    @Test
    fun `fetchProducts maps imagen_producto base name to images url`() = runTest {
        val dto = ProductoDto(
            codigoProducto = "2",
            nombreProducto = "B",
            precioProducto = 200,
            descripcionProducto = "d",
            imagenProducto = "cake-image",
            imagenUrl = null,
            stock = 10,
            stockCritico = null,
            categoriaId = null,
            categoriaNombre = "desserts"
        )

        val mockService = io.mockk.mockk<com.example.appmilsabores.network.ApiService>(relaxed = true)
        coEvery { mockService.getProductos() } returns listOf(dto)

        val ds = ProductRemoteDataSourceImpl(apiService = mockService)
        val products = ds.fetchProducts()
        assertEquals(1, products.size)
        val p = products[0]
        assertTrue(p.imageUrl!!.contains("/images/products/"))
        assertEquals(10, p.stock)
        assertEquals("2", p.codigo)
    }

    @Test
    fun `updateProductStock calls get and update and maps returned dto`() = runTest {
        val codigo = "C100"
        val current = ProductoDto(
            codigoProducto = codigo,
            nombreProducto = "X",
            precioProducto = 50,
            descripcionProducto = "x",
            imagenProducto = "img",
            imagenUrl = null,
            stock = 5,
            stockCritico = null,
            categoriaId = null,
            categoriaNombre = "c"
        )
        val updatedDto = current.copy(stock = 9)

        val mockService = io.mockk.mockk<com.example.appmilsabores.network.ApiService>(relaxed = true)
        coEvery { mockService.getProducto(codigo) } returns current
        coEvery { mockService.updateProducto(codigo, any()) } returns updatedDto

        val ds = ProductRemoteDataSourceImpl(apiService = mockService)
        val product = ds.updateProductStock(codigo, 9)
        assertNotNull(product)
        assertEquals(9, product!!.stock)
        assertEquals(codigo, product.codigo)
    }
}
