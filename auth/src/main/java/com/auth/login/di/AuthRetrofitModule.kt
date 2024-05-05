package com.auth.login.di

import com.auth.login.data.remote.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRetrofitModule {

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)
}