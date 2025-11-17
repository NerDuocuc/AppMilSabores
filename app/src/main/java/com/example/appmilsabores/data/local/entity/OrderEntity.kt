package com.example.appmilsabores.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appmilsabores.domain.model.Order

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "total") val total: String,
    @ColumnInfo(name = "item_count") val itemCount: Int
)

fun OrderEntity.toDomain(): Order = Order(
    id = id,
    date = date,
    status = status,
    total = total,
    itemCount = itemCount
)

fun Order.toEntity(userId: Long): OrderEntity = OrderEntity(
    id = id,
    userId = userId,
    date = date,
    status = status,
    total = total,
    itemCount = itemCount
)
