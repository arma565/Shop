package com.authentication.auth.di

import android.content.Context
import com.authentication.auth.data.config.AccountRecoveryPreferences
import com.authentication.auth.data.config.UserAutoLoginPreferencesRepository
import com.authentication.auth.data.remote.IAuthApiService
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
object UserModule {

    const val SERVER_URL = "http://10.0.2.2:5068"

    @Provides
    @Singleton
    fun provideUserAutoLoginPreferences(@ApplicationContext context: Context): UserAutoLoginPreferencesRepository {
        return UserAutoLoginPreferencesRepository(context)
    }

    @Provides
    @Singleton
    fun provideAccountRecoveryPreferences(@ApplicationContext context: Context): AccountRecoveryPreferences {
        return AccountRecoveryPreferences(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl(SERVER_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): IAuthApiService =
        retrofit.create(
            IAuthApiService::class.java
        )
}