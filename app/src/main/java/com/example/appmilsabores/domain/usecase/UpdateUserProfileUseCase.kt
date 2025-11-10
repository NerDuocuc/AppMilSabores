package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.repository.UserRepository

class UpdateUserProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(fullName: String, email: String, newPassword: String?) {
        repository.updateUser(fullName, email, newPassword)
    }
}
