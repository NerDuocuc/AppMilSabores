package com.example.appmilsabores.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
	@PrimaryKey val id: Int,
	@ColumnInfo(name = "codigo_producto") val codigo: String? = null,
	val name: String,
	val price: Double,
	@ColumnInfo(name = "old_price") val oldPrice: Double?,
	val rating: Float,
	val reviews: Int,
	@ColumnInfo(name = "image_res") val imageRes: Int,
	@ColumnInfo(name = "image_url") val imageUrl: String? = null,
	// stock for inventory (0..30)
	@ColumnInfo(name = "stock") val stock: Int = 0,
	val category: String,
	val description: String? = null
)