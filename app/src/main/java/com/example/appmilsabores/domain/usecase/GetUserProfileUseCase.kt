package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.Order
import com.example.appmilsabores.domain.model.UserProfile
import com.example.appmilsabores.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(private val repo: UserRepository) {
    fun observeProfile(): Flow<UserProfile?> = repo.observeUserProfile()
    suspend fun getUserProfile(): UserProfile? = repo.getUserProfile()
    suspend fun getOrders(): List<Order> = repo.getUserOrders()
    suspend fun logout() = repo.logout()
    suspend fun updateProfilePhoto(photoUri: String) = repo.updateProfilePhoto(photoUri)
}
