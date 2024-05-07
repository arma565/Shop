package com.store.shop.di

import android.content.Context
import androidx.room.Room
import com.store.shop.data.local.ShopDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    private const val APP_DATABASE_NAME = "Shop.db"

    @Provides
    @Singleton
    fun provideShopDatabase(@ApplicationContext context: Context): ShopDatabase =
        synchronized(ShopDatabase::class.java) {
            Room.databaseBuilder(context, ShopDatabase::class.java, APP_DATABASE_NAME)
                .build()
        }

    @Provides
    @Singleton
    fun provideShopDao(db: ShopDatabase) = db.dao()
}