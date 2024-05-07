package com.store.shop.data.repository

import com.store.shop.data.local.ShopDao
import com.store.shop.data.model.Product

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalShopRepository @Inject constructor(
    private val dao: ShopDao
) {
    suspend fun upsertProduct(product: Product) = dao.upsertProduct(product)

    fun productList(): Flow<List<Product>> = flow {
        this.emit(dao.productList())
        delay(1000L)
    }

    suspend fun deleteProduct(productId: Int) = dao.deleteProduct(productId)

    suspend fun deleteAllProducts() = dao.deleteAllProducts()

}