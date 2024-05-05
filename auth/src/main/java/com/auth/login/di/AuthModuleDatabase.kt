package com.auth.login.di

import android.content.Context
import androidx.room.Room
import com.auth.login.data.local.AuthDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModuleDatabase {

    private const val AUTH_DATABASE_NAME = "auth.db"

    @Singleton
    @Provides
    fun provideLoginDataBase(@ApplicationContext context: Context) =
        synchronized(AuthDatabase::class.java) {
            Room.databaseBuilder(context, AuthDatabase::class.java, AUTH_DATABASE_NAME).build()
        }

    @Singleton
    @Provides
    fun provideDao(db: AuthDatabase) = db.dao()
}