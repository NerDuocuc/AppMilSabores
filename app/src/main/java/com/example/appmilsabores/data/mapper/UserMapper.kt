package com.example.appmilsabores.data.mapper

import com.example.appmilsabores.R
import com.example.appmilsabores.data.local.entity.UserEntity
import com.example.appmilsabores.domain.model.User
import com.example.appmilsabores.domain.model.UserProfile

object UserMapper {
	fun toProfile(entity: UserEntity): UserProfile {
		// If avatar is missing, use a default profile picture (not avatar_placeholder)
		val avatar = entity.avatarRes ?: R.drawable.profile_picture_female
		return UserProfile(
			name = entity.fullName,
			email = entity.email,
			avatarRes = avatar,
			photoUri = entity.photoUri,
			orderCount = entity.orderCount,
			promoCode = entity.promoCode,
			run = entity.run,
			profileRole = entity.profileRole,
			birthDate = entity.birthDate,
			region = entity.region,
			comuna = entity.comuna,
			address = entity.address,
			hasLifetimeDiscount = entity.hasLifetimeDiscount,
			isSystem = entity.isSystem
		)
	}

	fun toUser(entity: UserEntity): User = User(
		id = entity.id,
		fullName = entity.fullName,
		email = entity.email,
		isSuperAdmin = entity.isSuperAdmin,
			avatarRes = entity.avatarRes,
			run = entity.run,
			firstName = entity.firstName,
			lastName = entity.lastName,
			profileRole = entity.profileRole,
			hasPassword = !entity.passwordHash.isNullOrBlank(),
			hasLifetimeDiscount = entity.hasLifetimeDiscount,
			isSystem = entity.isSystem
	)
}