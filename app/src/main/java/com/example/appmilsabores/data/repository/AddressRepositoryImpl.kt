package com.example.appmilsabores.data.repository

import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.data.local.entity.AddressEntity
import com.example.appmilsabores.data.local.dao.AddressDao
import com.example.appmilsabores.domain.model.Address
import com.example.appmilsabores.domain.repository.AddressRepository
import kotlinx.coroutines.runBlocking

class AddressRepositoryImpl(
    private val dao: AddressDao = AppMilSaboresApplication.database.addressDao(),
    private val userRepository: UserRepositoryImpl = UserRepositoryImpl()
) : AddressRepository {

    override fun getAddresses(): List<Address> = synchronized(this) {
        val persisted = runBlocking { dao.getAll() }
        if (persisted.isEmpty()) {
            bootstrapFromProfileLocked()
            return@synchronized addressesFromMemory()
        }
        return@synchronized persisted.map(AddressRepositoryImpl::toDomain)
    }

    override fun addAddress(
        alias: String,
        street: String,
        city: String,
        details: String,
        setAsDefault: Boolean
    ): Address = synchronized(this) {
        val entity = AddressEntity(
            alias = alias.trim(),
            street = street.trim(),
            city = city.trim(),
            details = details.trim(),
            isDefault = false,
            createdAt = System.currentTimeMillis()
        )
        val newId = runBlocking { dao.insert(entity) }
        val stored = runBlocking { dao.findById(newId) } ?: entity.copy(id = newId)

        if (setAsDefault || runBlocking { dao.getAll() }.none { it.isDefault }) {
            runBlocking { dao.setDefault(newId) }
            // persist selection to user profile and session prefs
            try {
                val addr = stored.street.takeIf { it.isNotBlank() }
                val comuna = stored.city.takeIf { it.isNotBlank() }
                runBlocking {
                    userRepository.setAddressForCurrentUser(addr, comuna, null)
                    AppMilSaboresApplication.sessionPreferences.savePrimaryAddress(addr, comuna, null)
                }
            } catch (_: Exception) {
            }
        }

        return@synchronized toDomain(stored)
    }

    override fun deleteAddress(id: Int) {
        synchronized(this) {
            val existing = runBlocking { dao.findById(id.toLong()) } ?: return@synchronized
            runBlocking { dao.deleteById(id.toLong()) }
            if (existing.isDefault) {
                val remaining = runBlocking { dao.getAll() }
                if (remaining.isNotEmpty()) {
                    runBlocking { dao.setDefault(remaining.first().id) }
                }
            }
        }
    }

    override fun setDefaultAddress(id: Int) {
        synchronized(this) {
            val exists = runBlocking { dao.findById(id.toLong()) } ?: return@synchronized
            runBlocking { dao.setDefault(id.toLong()) }
            try {
                val addr = exists.street.takeIf { it.isNotBlank() }
                val comuna = exists.city.takeIf { it.isNotBlank() }
                runBlocking {
                    userRepository.setAddressForCurrentUser(addr, comuna, null)
                    AppMilSaboresApplication.sessionPreferences.savePrimaryAddress(addr, comuna, null)
                }
            } catch (_: Exception) {
            }
        }
    }

    // Keep compatibility with older callers that expect to set a primary address in one call
    fun setPrimaryAddress(fullAddress: String) {
        synchronized(this) {
            runBlocking { dao.clearAll() }
            if (fullAddress.isNotBlank()) {
                val entity = AddressEntity(
                    alias = "Principal",
                    street = fullAddress.trim(),
                    city = "",
                    details = "",
                    isDefault = true,
                    createdAt = System.currentTimeMillis()
                )
                val newId = runBlocking { dao.insert(entity) }
                runBlocking { dao.setDefault(newId) }
                try {
                    val toPersist = entity.street.takeIf { it.isNotBlank() }
                    runBlocking {
                        userRepository.setAddressForCurrentUser(toPersist, null, null)
                        AppMilSaboresApplication.sessionPreferences.savePrimaryAddress(toPersist, null, null)
                    }
                } catch (_: Exception) {
                }
            } else {
                // cleared
                try {
                    runBlocking {
                        userRepository.setAddressForCurrentUser(null, null, null)
                        AppMilSaboresApplication.sessionPreferences.savePrimaryAddress(null, null, null)
                    }
                } catch (_: Exception) {}
            }
        }
    }

    fun clearAll() {
        synchronized(this) {
            runBlocking { dao.clearAll() }
            try {
                runBlocking {
                    userRepository.setAddressForCurrentUser(null, null, null)
                    AppMilSaboresApplication.sessionPreferences.savePrimaryAddress(null, null, null)
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun bootstrapFromProfileLocked() {
        // Try session prefs first
        try {
            val primary = runBlocking { AppMilSaboresApplication.sessionPreferences.readPrimaryAddress() }
            val addr = primary.first?.trim().orEmpty()
            if (addr.isNotEmpty()) {
                val entity = AddressEntity(
                    alias = "Principal",
                    street = addr,
                    city = primary.second?.trim().orEmpty(),
                    details = "",
                    isDefault = true,
                    createdAt = System.currentTimeMillis()
                )
                runBlocking { dao.insert(entity) }
                return
            }
        } catch (_: Exception) {
        }

        val profile = runBlocking { userRepository.getUserProfile() }
        val mainAddress = profile?.address?.trim().orEmpty()
        if (mainAddress.isEmpty()) return
        val entity = AddressEntity(
            alias = "Principal",
            street = mainAddress,
            city = profile?.comuna?.trim().orEmpty(),
            details = "",
            isDefault = true,
            createdAt = System.currentTimeMillis()
        )
        runBlocking { dao.insert(entity) }
    }

    private fun addressesFromMemory(): List<Address> {
        // read from DB and map to domain
        val persisted = runBlocking { dao.getAll() }
        return persisted.map(AddressRepositoryImpl::toDomain)
    }

    companion object {
        private fun toDomain(entity: AddressEntity) = Address(
            id = entity.id.toInt(),
            alias = entity.alias,
            street = entity.street,
            city = entity.city,
            details = entity.details,
            isDefault = entity.isDefault
        )
    }
}

