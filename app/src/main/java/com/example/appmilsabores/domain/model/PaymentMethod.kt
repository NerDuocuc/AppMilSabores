package com.example.appmilsabores.domain.model

enum class CardType { VISA, MASTERCARD, OTHER }

data class PaymentMethod(
    val id: Int,
    val cardType: CardType,
    val lastFourDigits: String,
    val expiryDate: String,
    val isDefault: Boolean,
    val cardHolderName: String
)
