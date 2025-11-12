package com.example.appmilsabores.domain.repository

import com.example.appmilsabores.domain.model.Order
import com.example.appmilsabores.domain.model.User
import com.example.appmilsabores.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUserProfile(): Flow<UserProfile?>
    suspend fun getUserProfile(): UserProfile?
    suspend fun getUserOrders(): List<Order>
    suspend fun addOrder(order: Order)
    suspend fun findUserByEmail(email: String): User?
    suspend fun authenticate(email: String, password: String): User?
    suspend fun register(
        firstName: String,
        lastName: String,
        run: String,
        email: String,
        password: String,
        birthDate: String,
        region: String,
        comuna: String,
        address: String,
        referralCode: String?
    ): User
    suspend fun setPromoCodeForCurrentUser(promoCode: String?)
    suspend fun logout()
    suspend fun updateUser(fullName: String, email: String, newPassword: String?)
    suspend fun updateProfilePhoto(photoUri: String)
}
