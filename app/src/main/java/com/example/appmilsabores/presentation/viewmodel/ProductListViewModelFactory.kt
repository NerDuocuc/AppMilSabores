package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appmilsabores.data.repository.CartRepositoryImpl
import com.example.appmilsabores.data.repository.ProductRepositoryImpl
import com.example.appmilsabores.data.repository.SessionRepositoryImpl
import com.example.appmilsabores.domain.usecase.AddToCartUseCase
import com.example.appmilsabores.domain.usecase.GetProductsByCategoryUseCase

class ProductListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = ProductRepositoryImpl()
        val getProducts = GetProductsByCategoryUseCase(repo)
        val addToCart = AddToCartUseCase(CartRepositoryImpl(), SessionRepositoryImpl())
        return ProductListViewModel(getProducts, addToCart) as T
    }
}
