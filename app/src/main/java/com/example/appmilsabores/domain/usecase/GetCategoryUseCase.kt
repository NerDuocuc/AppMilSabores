package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.CategoryInfo
import com.example.appmilsabores.domain.repository.CategoryRepository

class GetCategoriesUseCase(private val repo: CategoryRepository) {
    operator fun invoke(): List<CategoryInfo> = repo.getAllCategories()
}
