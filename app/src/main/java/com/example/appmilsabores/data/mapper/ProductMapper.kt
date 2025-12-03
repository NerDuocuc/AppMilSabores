package com.example.appmilsabores.data.mapper

import com.example.appmilsabores.data.local.entity.ProductEntity
import com.example.appmilsabores.domain.model.Product

object ProductMapper {

	fun toDomain(entity: ProductEntity): Product {
		return Product(
			id = entity.id,
			name = entity.name,
			price = entity.price,
			oldPrice = entity.oldPrice,
			rating = entity.rating,
			reviews = entity.reviews,
			imageRes = entity.imageRes,
			imageUrl = entity.imageUrl,
			stock = entity.stock,
			category = entity.category,
			description = entity.description.orEmpty(),
			codigo = entity.codigo
		)
	}

		fun toEntity(product: Product): ProductEntity {
		fun normalizeCategory(name: String): String {
			val normalized = java.text.Normalizer.normalize(name, java.text.Normalizer.Form.NFD)
			return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "").trim().lowercase()
		}

		return ProductEntity(
			id = product.id,
			codigo = product.codigo,
			name = product.name,
			price = product.price,
			oldPrice = product.oldPrice,
			rating = product.rating,
			reviews = product.reviews,
				imageRes = product.imageRes,
				imageUrl = product.imageUrl,
				stock = product.stock,
				category = normalizeCategory(product.category),
				description = product.description
		)
	}
}