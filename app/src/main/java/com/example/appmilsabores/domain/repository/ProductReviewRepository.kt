package com.example.appmilsabores.domain.repository

import com.example.appmilsabores.domain.model.ProductReview

interface ProductReviewRepository {
    suspend fun getReviewsForProduct(productId: Int): List<ProductReview>
}
