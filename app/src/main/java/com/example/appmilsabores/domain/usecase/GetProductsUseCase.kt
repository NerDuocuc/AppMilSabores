package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val repository: ProductRepository) {
	operator fun invoke(): Flow<List<Product>> = repository.observeProducts()
}