package com.example.appmilsabores.data.mapper

import com.example.appmilsabores.data.local.dao.CartItemWithProduct
import com.example.appmilsabores.data.local.entity.CartItemEntity
import com.example.appmilsabores.domain.model.CartItem

object CartMapper {

	fun toDomain(relation: CartItemWithProduct): CartItem {
		return CartItem(
			id = relation.product.id,
			name = relation.product.name,
			price = relation.product.price,
			imageRes = relation.product.imageRes,
			quantity = relation.cart.quantity
		)
	}

	fun toEntity(cartItem: CartItem, userId: Long): CartItemEntity {
		return CartItemEntity(
			userId = userId,
			productId = cartItem.id,
			quantity = cartItem.quantity
		)
	}
}