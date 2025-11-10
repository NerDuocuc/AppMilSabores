package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.model.ProductFilters
import com.example.appmilsabores.domain.repository.ProductRepository

class GetProductsByCategoryUseCase(private val repo: ProductRepository) {
    suspend operator fun invoke(categoryName: String, filters: ProductFilters = ProductFilters()): List<Product> {
        val mergedFilters = if (filters.categories.isEmpty()) {
            filters.copy(categories = setOf(categoryName))
        } else {
            filters
        }
        return repo.getProductsByCategory(categoryName, mergedFilters)
    }
}
