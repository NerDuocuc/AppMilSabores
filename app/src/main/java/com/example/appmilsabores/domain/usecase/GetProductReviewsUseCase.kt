package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.ProductReview
import com.example.appmilsabores.domain.repository.ProductReviewRepository

class GetProductReviewsUseCase(private val repository: ProductReviewRepository) {
    suspend operator fun invoke(productId: Int): List<ProductReview> {
        return repository.getReviewsForProduct(productId)
    }
}
