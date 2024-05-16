package com.store.shop.di

import android.content.Context
import androidx.room.Room
import com.auth.login.data.local.AuthDatabase
import com.auth.login.data.model.Constants
import com.auth.login.data.model.Constants.APP_DATABASE_NAME
import com.auth.login.data.model.Constants.BASE_URL
import com.auth.login.data.remote.AuthApiService
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


    @Singleton
    @Provides
    fun provideLoginDataBase(@ApplicationContext context: Context) =
        synchronized(AuthDatabase::class.java) {
            Room.databaseBuilder(context, AuthDatabase::class.java, Constants.AUTH_DATABASE_NAME).build()
        }

    @Singleton
    @Provides
    fun provideDao(db: AuthDatabase) = db.dao()

    @Singleton
    @Provides
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

}