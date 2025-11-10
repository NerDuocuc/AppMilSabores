package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmilsabores.data.repository.ProductRepositoryImpl
import com.example.appmilsabores.domain.model.Product
import com.example.appmilsabores.domain.model.ProductReview
import com.example.appmilsabores.data.repository.CartRepositoryImpl
import com.example.appmilsabores.data.repository.SessionRepositoryImpl
import com.example.appmilsabores.data.repository.ProductReviewRepositoryImpl
import com.example.appmilsabores.domain.usecase.AddToCartUseCase
import com.example.appmilsabores.domain.usecase.GetProductByIdUseCase
import com.example.appmilsabores.domain.usecase.GetProductReviewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductDetailUiState(
    val product: Product? = null,
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val addedToCart: Boolean = false,
    val reviews: List<ProductReview> = emptyList()
) {
    val totalPrice: Double get() = (product?.price ?: 0.0) * quantity
}

class ProductDetailViewModel(
    private val getProductById: GetProductByIdUseCase = GetProductByIdUseCase(ProductRepositoryImpl()),
    private val addToCart: AddToCartUseCase = AddToCartUseCase(
        CartRepositoryImpl(),
        SessionRepositoryImpl()
    ),
    private val getProductReviews: GetProductReviewsUseCase = GetProductReviewsUseCase(ProductReviewRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            try {
                val result = getProductById(productId)
                val reviews = result?.let { getProductReviews(productId) }.orEmpty()
                _uiState.update {
                    it.copy(product = result, isLoading = false, reviews = reviews)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }
    }

    fun increaseQuantity() {
        _uiState.update { it.copy(quantity = it.quantity + 1) }
    }

    fun decreaseQuantity() {
        _uiState.update {
            if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else it
        }
    }

    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    fun addCurrentSelectionToCart() {
        val productId = _uiState.value.product?.id ?: return
        val quantity = _uiState.value.quantity

        viewModelScope.launch {
            addToCart(productId, quantity)
            _uiState.update { it.copy(addedToCart = true) }
        }
    }

    fun consumeAddedToCartFlag() {
        if (_uiState.value.addedToCart) {
            _uiState.update { it.copy(addedToCart = false) }
        }
    }
}
