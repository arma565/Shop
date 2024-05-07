package com.store.shop.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.store.shop.data.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ShopDatabase : RoomDatabase() {
    abstract fun dao(): ShopDao
}