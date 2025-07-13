package com.store.shop.di

import android.content.Context
import androidx.room.Room
import com.store.shop.data.local.ShopDatabase
import com.store.shop.data.remote.ShopApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    const val APP_DATABASE_NAME = "shop.db"

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

    @Singleton
    @Provides
    fun provideShopApiService(retrofit: Retrofit): ShopApiService =
        retrofit.create(ShopApiService::class.java)
}