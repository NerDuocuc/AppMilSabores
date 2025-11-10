package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmilsabores.data.repository.CategoryRepositoryImpl
import com.example.appmilsabores.domain.model.CategoryInfo
import com.example.appmilsabores.domain.usecase.GetCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryUiState(
    val categories: List<CategoryInfo> = emptyList(),
    val isLoading: Boolean = true
)

class CategoryViewModel(
    private val getCategories: GetCategoriesUseCase = GetCategoriesUseCase(CategoryRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(categories = getCategories(), isLoading = false)
            }
        }
    }
}
