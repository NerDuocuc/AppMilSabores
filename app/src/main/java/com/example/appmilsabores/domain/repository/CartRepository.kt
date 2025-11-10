package com.example.appmilsabores.domain.repository

import com.example.appmilsabores.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCartItems(userId: Long): Flow<List<CartItem>>
    suspend fun addProduct(userId: Long, productId: Int, quantity: Int = 1)
    suspend fun updateQuantity(userId: Long, productId: Int, quantity: Int)
    suspend fun removeItem(userId: Long, productId: Int)
    suspend fun clearCart(userId: Long)
}
