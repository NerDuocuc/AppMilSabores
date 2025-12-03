package com.example.appmilsabores.data.repository

import com.example.appmilsabores.data.local.dao.ProductDao
import com.example.appmilsabores.data.local.entity.ProductEntity
import com.example.appmilsabores.data.source.ProductRemoteDataSource
import com.example.appmilsabores.domain.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class ProductRepositoryImplTest {

    private class FakeDao : ProductDao {
        var lastUpsert: List<ProductEntity>? = null
        var stored: MutableMap<Int, ProductEntity> = mutableMapOf()

        override fun observeProducts() = throw UnsupportedOperationException()
        override suspend fun getAllProducts(): List<ProductEntity> = stored.values.toList()
        override suspend fun getProductsByCategory(category: String): List<ProductEntity> = stored.values.filter { it.category == category }
        override suspend fun getProductsByCategories(categories: List<String>): List<ProductEntity> = stored.values.filter { categories.contains(it.category) }
        override suspend fun getProductById(productId: Int): ProductEntity? = stored[productId]
        override suspend fun searchProducts(query: String): List<ProductEntity> = stored.values.filter { it.name.contains(query, ignoreCase = true) }
        override suspend fun upsertProducts(products: List<ProductEntity>) {
            lastUpsert = products
            products.forEach { stored[it.id] = it }
        }
        override suspend fun countProducts(): Int = stored.size
        override suspend fun clearProducts() { stored.clear() }
        override suspend fun updateStock(productId: Int, newStock: Int) { stored[productId]?.let { stored[productId] = it.copy(stock = newStock) } }
    }

    @Test
    fun `updateProductStock uses remote when available and persists returned product`() = runTest {
        val fakeDao = FakeDao()
        val id = 10
        val initial = ProductEntity(id = id, codigo = "K10", name = "P", price = 1.0, oldPrice = null, rating = 0f, reviews = 0, imageRes = 0, imageUrl = null, stock = 2, category = "c", description = "d")
        fakeDao.stored[id] = initial

        val remote = mockk<ProductRemoteDataSource>()
        val serverProduct = Product(id = id, name = "P", price = 1.0, rating = 0f, reviews = 0, imageRes = 0, imageUrl = null, stock = 8, category = "c", description = "d", codigo = "K10")
        coEvery { remote.updateProductStock("K10", 8) } returns serverProduct

        val repo = ProductRepositoryImpl(productDao = fakeDao, remoteDataSource = remote)
        val ok = repo.updateProductStock(id, 8)
        assertTrue(ok)
        // remote should have been called
        coVerify { remote.updateProductStock("K10", 8) }
        // DAO should have upserted the returned product
        assertNotNull(fakeDao.lastUpsert)
        assertEquals(8, fakeDao.stored[id]?.stock)
    }

    @Test
    fun `updateProductStock falls back to local when remote unavailable`() = runTest {
        val fakeDao = FakeDao()
        val id = 11
        val initial = ProductEntity(id = id, codigo = null, name = "Q", price = 2.0, oldPrice = null, rating = 0f, reviews = 0, imageRes = 0, imageUrl = null, stock = 3, category = "c", description = "d")
        fakeDao.stored[id] = initial

        val repo = ProductRepositoryImpl(productDao = fakeDao, remoteDataSource = null)
        val ok = repo.updateProductStock(id, 4)
        assertTrue(ok)
        assertEquals(4, fakeDao.stored[id]?.stock)
    }
}
