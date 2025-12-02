package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmilsabores.data.repository.ProductRepositoryImpl
import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.usecase.UpdateProductStockUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class AdminProductsUiState(
    val products: List<Product> = emptyList()
)

class AdminProductsViewModel(
    private val repository: ProductRepositoryImpl = ProductRepositoryImpl(),
    private val updateStock: UpdateProductStockUseCase = UpdateProductStockUseCase(repository)
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminProductsUiState())
    val uiState: StateFlow<AdminProductsUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.observeProducts().collectLatest { list ->
                _uiState.value = _uiState.value.copy(products = list)
            }
        }
    }

    fun setStock(productId: Int, stock: Int) {
        viewModelScope.launch {
            updateStock(productId, stock)
        }
    }

    fun incrementStock(productId: Int) {
        val product = _uiState.value.products.firstOrNull { it.id == productId } ?: return
        setStock(productId, product.stock + 1)
    }

    fun decrementStock(productId: Int) {
        val product = _uiState.value.products.firstOrNull { it.id == productId } ?: return
        val newStock = (product.stock - 1).coerceAtLeast(0)
        setStock(productId, newStock)
    }
}
