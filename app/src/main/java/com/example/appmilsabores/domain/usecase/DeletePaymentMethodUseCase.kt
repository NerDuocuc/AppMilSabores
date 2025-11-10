package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.repository.PaymentRepository

class DeletePaymentMethodUseCase(private val repo: PaymentRepository) {
    suspend operator fun invoke(id: Int) = repo.deletePaymentMethod(id)
}
