package com.example.appmilsabores.domain.repository

import com.example.appmilsabores.domain.model.*

interface LandingRepository {
    fun getPromotions(): List<Promotion>
    fun getCategories(): List<Category>
    fun getFeaturedProducts(): List<ProductSummary>
    fun getNewProducts(): List<ProductSummary>
}
