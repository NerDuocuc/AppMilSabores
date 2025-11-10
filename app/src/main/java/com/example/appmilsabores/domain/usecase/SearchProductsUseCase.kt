package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.model.ProductFilters
import com.example.appmilsabores.domain.repository.ProductRepository

class SearchProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(query: String, filters: ProductFilters = ProductFilters()): List<Product> {
        if (query.isBlank()) return emptyList()
        return repository.searchProducts(query.trim(), filters)
    }
}
