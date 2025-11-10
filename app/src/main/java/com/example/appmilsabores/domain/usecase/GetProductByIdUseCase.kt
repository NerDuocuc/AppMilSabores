package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.repository.ProductRepository

class GetProductByIdUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(productId: Int): Product? {
        return repository.getProductById(productId)
    }
}
