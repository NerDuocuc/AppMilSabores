package com.example.appmilsabores.data.repository

import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.data.local.dao.ProductDao
import com.example.appmilsabores.data.mapper.ProductMapper
import com.example.appmilsabores.data.source.ProductRemoteDataSource
import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.model.ProductFilters
import com.example.appmilsabores.domain.model.ProductSortOption
import com.example.appmilsabores.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao = AppMilSaboresApplication.database.productDao(),
    private val remoteDataSource: ProductRemoteDataSource? = null
) : ProductRepository {

    override fun observeProducts(): Flow<List<Product>> {
        return productDao.observeProducts().map { list ->
            list.map(ProductMapper::toDomain)
        }
    }

    override suspend fun getProducts(filters: ProductFilters): List<Product> {
        val local = loadLocalProducts(filters)
        if (local.isNotEmpty() || remoteDataSource == null) {
            return local
        }

        fetchRemoteAndCache()
        return loadLocalProducts(filters)
    }

    override suspend fun getProductsByCategory(categoryName: String, filters: ProductFilters): List<Product> {
        val mergedFilters = filters.ensureCategory(categoryName)
        val local = loadLocalProducts(mergedFilters)
        if (local.isNotEmpty() || remoteDataSource == null) {
            return local
        }

        fetchRemoteAndCache()
        return loadLocalProducts(mergedFilters)
    }

    override suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)?.let(ProductMapper::toDomain)
    }

    override suspend fun updateProductStock(id: Int, newStock: Int): Boolean {
        return try {
            val existing = productDao.getProductById(id)

            // If we have a remote data source and a server product code, try to update remote first
            val remoteUpdated = try {
                val codigo = existing?.codigo
                if (remoteDataSource != null && codigo != null) {
                    val remoteProduct = remoteDataSource.updateProductStock(codigo, newStock)
                    if (remoteProduct != null) {
                        // persist returned product from server to local DB
                        val entity = ProductMapper.toEntity(remoteProduct)
                        productDao.upsertProducts(listOf(entity))
                        android.util.Log.d("ProductRepository", "updateStock id=$id -> remote persisted stock=${remoteProduct.stock}")
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            } catch (t: Throwable) {
                android.util.Log.w("ProductRepository", "Remote update failed for id=$id", t)
                false
            }

            if (!remoteUpdated) {
                // Fallback to local-only update
                productDao.updateStock(id, newStock)
                try {
                    val after = productDao.getProductById(id)
                    android.util.Log.d("ProductRepository", "updateStock id=$id -> requested=$newStock persisted=${after?.stock}")
                } catch (t: Throwable) {
                    android.util.Log.w("ProductRepository", "updateStock: failed to read back product id=$id", t)
                }
            }

            true
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "Failed to update stock for id=$id", e)
            false
        }
    }

    override suspend fun searchProducts(query: String, filters: ProductFilters): List<Product> {
        val sanitized = query.trim()
        if (sanitized.isEmpty()) return emptyList()

        val local = loadLocalProducts(filters, sanitized)
        if (local.isNotEmpty() || remoteDataSource == null) {
            return local
        }

        val remoteProducts = fetchRemoteAndCache()
        return remoteProducts
            .filterByQuery(sanitized)
            .applyFilters(filters)
    }

    private suspend fun loadLocalProducts(filters: ProductFilters, query: String? = null): List<Product> {
        val entities = when {
            query != null -> productDao.searchProducts(query)
            filters.categories.size == 1 -> productDao.getProductsByCategory(filters.categories.first())
            filters.categories.isNotEmpty() -> productDao.getProductsByCategories(filters.categories.toList())
            else -> productDao.getAllProducts()
        }

        val products = entities.map(ProductMapper::toDomain)
        val filtered = products.applyFilters(filters)
        return if (query != null) filtered.filterByQuery(query) else filtered
    }

    private suspend fun fetchRemoteAndCache(): List<Product> {
        val remoteProducts = remoteDataSource?.fetchProducts().orEmpty()
        if (remoteProducts.isNotEmpty()) {
            // Temporary debug logging to diagnose category issues
            try {
                android.util.Log.d("ProductRepository", "Fetched ${remoteProducts.size} remote products")
                val entities = remoteProducts.map { prod ->
                    val entity = ProductMapper.toEntity(prod)
                    android.util.Log.d("ProductRepository", "Remote product='${prod.name}' originalCategory='${prod.category}' storedCategory='${entity.category}'")
                    entity
                }

                productDao.upsertProducts(entities)

                // Log categories currently stored in DB
                val stored = productDao.getAllProducts()
                val categories = stored.map { it.category }.distinct()
                android.util.Log.d("ProductRepository", "Stored product count=${stored.size}, categories=${categories}")
            } catch (e: Exception) {
                android.util.Log.e("ProductRepository", "Error while caching remote products", e)
            }
        }
        return remoteProducts
    }

    private fun List<Product>.applyFilters(filters: ProductFilters): List<Product> {
        val filtered = this
            .filter { filters.categories.isEmpty() || filters.categories.contains(it.category) }
            .filter { filters.minPrice?.let { min -> it.price >= min } ?: true }
            .filter { filters.maxPrice?.let { max -> it.price <= max } ?: true }
            .filter { filters.minRating?.let { rating -> it.rating >= rating } ?: true }
            .filter { if (filters.onSaleOnly) it.oldPrice != null else true }

        return if (filters.sortOption == ProductSortOption.RELEVANCE) {
            filtered
        } else {
            filtered.sortedWith(filters.sortOption.toComparator())
        }
    }

    private fun List<Product>.filterByQuery(query: String): List<Product> {
        val lowerQuery = query.lowercase()
        return filter { product ->
            product.name.lowercase().contains(lowerQuery) ||
                product.description.lowercase().contains(lowerQuery)
        }
    }

    private fun ProductFilters.ensureCategory(categoryName: String): ProductFilters {
        fun normalizeCategory(name: String): String {
            val normalized = java.text.Normalizer.normalize(name, java.text.Normalizer.Form.NFD)
            return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "").trim().lowercase()
        }

        // Normalize any categories already present in the filters, and also normalize the
        // incoming categoryName. This makes category matching insensitive to accents/case.
        val normalizedFromParam = normalizeCategory(categoryName)
        val normalizedCategories = if (categories.isEmpty()) {
            setOf(normalizedFromParam)
        } else {
            categories.map { normalizeCategory(it) }.toSet()
        }

        return copy(categories = normalizedCategories)
    }

    private fun ProductSortOption.toComparator(): Comparator<Product> {
        return when (this) {
            ProductSortOption.RELEVANCE -> Comparator { _, _ -> 0 }
            ProductSortOption.PRICE_ASC -> compareBy { it.price }
            ProductSortOption.PRICE_DESC -> compareByDescending { it.price }
            ProductSortOption.RATING_DESC -> compareByDescending<Product> { it.rating }
                .thenByDescending { it.reviews }
        }
    }
}
