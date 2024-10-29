package com.store.shop.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.store.shop.data.model.Product
import retrofit2.Response

@Dao
interface ShopDao {

    @Upsert
    suspend fun upsertProduct(product: Product)

    @Query("SELECT * FROM tbl_product ORDER BY id DESC")
     fun productList(): List<Product>

    @Query("DELETE FROM tbl_product WHERE id=:id")
    suspend fun deleteProduct(id : Int)

    @Query("DELETE FROM tbl_product")
    suspend fun deleteAllProducts()
}