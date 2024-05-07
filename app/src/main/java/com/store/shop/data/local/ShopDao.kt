package com.store.shop.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.store.shop.data.model.Product

@Dao
interface ShopDao {

    @Upsert
    suspend fun upsertProduct(product: Product)

    @Query("SELECT * FROM tbl_product ORDER BY id DESC")
    fun productList(): List<Product>

    @Query("DELETE FROM tbl_product WHERE productId=:productId")
    suspend fun deleteProduct(productId : Int)

    @Query("DELETE FROM tbl_product")
    suspend fun deleteAllProducts()
}