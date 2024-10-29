package com.store.shop.di

import android.content.Context
import androidx.room.Room
import com.authentication.auth.data.model.Constants.APP_DATABASE_NAME
import com.authentication.auth.data.model.Constants.BASE_URL
import com.store.shop.data.local.ShopDatabase
import com.store.shop.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}