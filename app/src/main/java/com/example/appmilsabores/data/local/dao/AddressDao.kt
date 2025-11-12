package com.example.appmilsabores.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.appmilsabores.data.local.entity.AddressEntity

@Dao
interface AddressDao {

    @Insert
    suspend fun insert(address: AddressEntity): Long

    @Query("SELECT * FROM addresses ORDER BY isDefault DESC, createdAt DESC")
    suspend fun getAll(): List<AddressEntity>

    @Query("SELECT * FROM addresses WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): AddressEntity?

    @Query("DELETE FROM addresses WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE addresses SET isDefault = 0")
    suspend fun clearDefaults()

    @Query("UPDATE addresses SET isDefault = 1 WHERE id = :id")
    suspend fun markAsDefault(id: Long)

    @Query("DELETE FROM addresses")
    suspend fun clearAll()

    @Transaction
    suspend fun setDefault(id: Long) {
        clearDefaults()
        markAsDefault(id)
    }
}
