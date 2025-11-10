package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.repository.CartRepository
import com.example.appmilsabores.domain.repository.SessionRepository

class UpdateCartItemQuantityUseCase(
    private val cartRepository: CartRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(productId: Int, quantity: Int) {
        val session = sessionRepository.getSession()
        val userId = session.userId ?: return
        cartRepository.updateQuantity(userId, productId, quantity)
    }
}
