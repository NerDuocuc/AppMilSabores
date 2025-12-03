package com.example.appmilsabores.data

import com.example.appmilsabores.AppMilSaboresApplication
import com.example.appmilsabores.data.repository.ProductRepositoryImpl
import com.example.appmilsabores.data.source.ProductRemoteDataSourceImpl

object AppDependencyContainer {

    // singletons for simple manual DI
    private val productRemoteDataSource by lazy { ProductRemoteDataSourceImpl() }

    fun createProductRepository(): ProductRepositoryImpl {
        return ProductRepositoryImpl(
            productDao = AppMilSaboresApplication.database.productDao(),
            remoteDataSource = productRemoteDataSource
        )
    }
}
