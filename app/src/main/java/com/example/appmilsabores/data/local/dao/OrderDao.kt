package com.example.appmilsabores.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appmilsabores.data.local.entity.OrderEntity

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY date DESC")
    suspend fun getOrdersForUser(userId: Long): List<OrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)
}
