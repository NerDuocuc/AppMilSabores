package com.example.appmilsabores.data.repository

import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.data.local.dao.CartDao
import com.example.appmilsabores.data.local.dao.ProductDao
import com.example.appmilsabores.data.local.entity.CartItemEntity
import com.example.appmilsabores.data.mapper.CartMapper
import com.example.appmilsabores.domain.model.CartItem
import com.example.appmilsabores.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private val cartDao: CartDao = AppMilSaboresApplication.database.cartDao(),
    private val productDao: ProductDao = AppMilSaboresApplication.database.productDao()
) : CartRepository {

    override fun observeCartItems(userId: Long): Flow<List<CartItem>> {
        return cartDao.observeCartItems(userId).map { relations ->
            relations.map(CartMapper::toDomain)
        }
    }

    override suspend fun addProduct(userId: Long, productId: Int, quantity: Int) {
        productDao.getProductById(productId)
            ?: throw IllegalArgumentException("Producto inexistente con id=$productId")

        val current = cartDao.getByProductId(userId, productId)
        val newQuantity = (current?.quantity ?: 0) + quantity
        cartDao.upsert(CartItemEntity(userId = userId, productId = productId, quantity = newQuantity))
    }

    override suspend fun updateQuantity(userId: Long, productId: Int, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteByProductId(userId, productId)
            return
        }

        val current = cartDao.getByProductId(userId, productId)
        if (current == null) {
            cartDao.upsert(CartItemEntity(userId = userId, productId = productId, quantity = quantity))
        } else {
            cartDao.updateQuantity(userId, productId, quantity)
        }
    }

    override suspend fun removeItem(userId: Long, productId: Int) {
        cartDao.deleteByProductId(userId, productId)
    }

    override suspend fun clearCart(userId: Long) {
        cartDao.clear(userId)
    }
}
