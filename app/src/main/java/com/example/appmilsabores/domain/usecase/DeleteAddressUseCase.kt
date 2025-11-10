package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.domain.repository.AddressRepository

class DeleteAddressUseCase(private val repo: AddressRepository) {
    operator fun invoke(id: Int) = repo.deleteAddress(id)
}
