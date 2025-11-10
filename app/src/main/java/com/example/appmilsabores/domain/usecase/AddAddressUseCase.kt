package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.Address
import com.example.appmilsabores.domain.repository.AddressRepository

class AddAddressUseCase(private val repository: AddressRepository) {
    operator fun invoke(
        alias: String,
        street: String,
        city: String,
        details: String,
        setAsDefault: Boolean
    ): Address {
        return repository.addAddress(alias, street, city, details, setAsDefault)
    }
}
