package com.example.appmilsabores.domain.repository

import com.example.appmilsabores.domain.model.CategoryInfo

interface CategoryRepository {
    fun getAllCategories(): List<CategoryInfo>
}
