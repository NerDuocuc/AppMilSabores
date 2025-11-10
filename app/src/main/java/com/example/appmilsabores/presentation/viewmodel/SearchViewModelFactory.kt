package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appmilsabores.data.repository.CartRepositoryImpl
import com.example.appmilsabores.data.repository.ProductRepositoryImpl
import com.example.appmilsabores.data.repository.SessionRepositoryImpl
import com.example.appmilsabores.domain.usecase.AddToCartUseCase
import com.example.appmilsabores.domain.usecase.SearchProductsUseCase

class SearchViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val productRepository = ProductRepositoryImpl()
        val searchProductsUseCase = SearchProductsUseCase(productRepository)
        val addToCartUseCase = AddToCartUseCase(CartRepositoryImpl(), SessionRepositoryImpl())
        return SearchViewModel(searchProductsUseCase, addToCartUseCase) as T
    }
}
