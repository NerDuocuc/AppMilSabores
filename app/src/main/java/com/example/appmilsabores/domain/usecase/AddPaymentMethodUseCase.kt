package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.CardType
import com.example.appmilsabores.domain.model.PaymentMethod
import com.example.appmilsabores.domain.repository.PaymentRepository


class AddPaymentMethodUseCase(private val repo: PaymentRepository) {
    suspend operator fun invoke(
        name: String,
        number: String,
        expiry: String
    ): PaymentMethod {
        val cardType = when {
            number.startsWith("4") -> CardType.VISA
            number.startsWith("5") -> CardType.MASTERCARD
            else -> CardType.OTHER
        }

        val lastFour = number.takeLast(4)
        val method = PaymentMethod(
            id = 0,
            cardType = cardType,
            lastFourDigits = lastFour,
            expiryDate = expiry,
            isDefault = false,
            cardHolderName = name
        )
        return repo.addPaymentMethod(method)
    }
}
