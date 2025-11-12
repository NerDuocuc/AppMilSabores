package com.example.appmilsabores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val alias: String,
    val street: String,
    val city: String,
    val details: String,
    val isDefault: Boolean,
    val createdAt: Long
)
