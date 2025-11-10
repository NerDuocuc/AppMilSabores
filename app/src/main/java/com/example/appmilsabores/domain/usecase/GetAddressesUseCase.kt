package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.model.Address
import com.example.appmilsabores.domain.repository.AddressRepository

class GetAddressesUseCase(private val repo: AddressRepository) {
    operator fun invoke(): List<Address> = repo.getAddresses()
}
