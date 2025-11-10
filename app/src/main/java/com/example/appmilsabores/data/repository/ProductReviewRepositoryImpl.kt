package com.example.appmilsabores.data.repository

import com.example.appmilsabores.data.local.seed.LocalSeedData
import com.example.appmilsabores.domain.model.ProductReview
import com.example.appmilsabores.domain.repository.ProductReviewRepository

class ProductReviewRepositoryImpl : ProductReviewRepository {
    override suspend fun getReviewsForProduct(productId: Int): List<ProductReview> {
        return LocalSeedData.productReviews[productId].orEmpty()
    }
}
