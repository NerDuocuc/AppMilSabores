package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmilsabores.data.AppDependencyContainer
import com.example.appmilsabores.domain.model.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminStockViewModel(
    private val productRepo: com.example.appmilsabores.domain.repository.ProductRepository = AppDependencyContainer.createProductRepository()
) : ViewModel() {

    val products: StateFlow<List<Product>> = productRepo.observeProducts()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    suspend fun confirmStockChange(productId: Int, newStock: Int): Boolean {
        // ensure bounds
        val bounded = newStock.coerceIn(0, 30)
        return try {
            productRepo.updateProductStock(productId, bounded)
        } catch (t: Throwable) {
            false
        }
    }

    fun getProductByIdSync(productId: Int, onResult: (Product?) -> Unit) {
        viewModelScope.launch {
            val p = productRepo.getProductById(productId)
            onResult(p)
        }
    }
}
