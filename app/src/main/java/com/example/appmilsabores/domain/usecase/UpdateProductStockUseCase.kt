package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.repository.ProductRepository

class UpdateProductStockUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(productId: Int, newStock: Int) {
        repository.updateProductStock(productId, newStock)
    }
}
