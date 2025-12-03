package com.example.appmilsabores.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appmilsabores.data.local.dao.CartDao
import com.example.appmilsabores.data.local.dao.PaymentMethodDao
import com.example.appmilsabores.data.local.dao.AddressDao
import com.example.appmilsabores.data.local.dao.ProductDao
import com.example.appmilsabores.data.local.dao.UserDao
import com.example.appmilsabores.data.local.entity.CartItemEntity
import com.example.appmilsabores.data.local.entity.PaymentMethodEntity
import com.example.appmilsabores.data.local.entity.ProductEntity
import com.example.appmilsabores.data.local.entity.UserEntity
import com.example.appmilsabores.data.local.seed.LocalSeedData

@Database(
	entities = [
		ProductEntity::class,
		CartItemEntity::class,
		UserEntity::class,
		PaymentMethodEntity::class,
		com.example.appmilsabores.data.local.entity.AddressEntity::class,
		com.example.appmilsabores.data.local.entity.OrderEntity::class
	],
	// bumped version to 10 to include codigo column
	version = 10,
	exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

	abstract fun productDao(): ProductDao
	abstract fun cartDao(): CartDao
	abstract fun userDao(): UserDao
	abstract fun paymentMethodDao(): PaymentMethodDao
	abstract fun addressDao(): AddressDao
	abstract fun orderDao(): com.example.appmilsabores.data.local.dao.OrderDao

	suspend fun seed() {
		// Do not seed products locally. Products will be synchronized from the remote API.
		val productDao = productDao()

		val userDao = userDao()
		LocalSeedData.seededUsers.forEach { seed ->
			val existing = userDao.findByEmail(seed.email)
			if (existing == null) {
				userDao.insertUser(seed.copy(id = 0))
			}
		}

		if (userDao.countSuperAdmins() == 0) {
			userDao.insertUser(LocalSeedData.superAdmin.copy(id = 0))
		}
	}

	companion object {
		private const val DB_NAME = "pasteleria_mil_sabores.db"

		@Volatile
		private var instance: AppDatabase? = null

		fun build(context: Context): AppDatabase {
			return instance ?: synchronized(this) {
				instance ?: Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					DB_NAME
				).fallbackToDestructiveMigration()
					.build()
					.also { instance = it }
			}
		}
	}
}