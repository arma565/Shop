package com.authentication.auth.di

import android.content.Context
import androidx.room.Room
import com.authentication.auth.data.local.UserDatabase
import com.authentication.auth.data.remote.AuthApiService
import com.authentication.auth.data.use_case.ValidateTerms
import com.authentication.auth.data.use_case.ValidateUserEmail
import com.authentication.auth.data.use_case.ValidateUserAlreadyExist
import com.authentication.auth.data.use_case.ValidateUserNotFound
import com.authentication.auth.data.use_case.ValidateUserPassword
import com.authentication.auth.data.use_case.ValidateUserRecoveryCode
import com.authentication.auth.data.use_case.ValidateUserRecoveryCodeNotExist
import com.authentication.auth.data.use_case.ValidateUserRepeatedPassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    private const val DB_NAME = "login.db"

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context) =
        synchronized(UserDatabase::class.java) {
            Room.databaseBuilder(context, UserDatabase::class.java, DB_NAME).build()
        }

    @Singleton
    @Provides
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Singleton
    @Provides
    fun provideUserDao(db: UserDatabase) = db.userDao()

    @Provides
    fun provideValidationUserEmail(@ApplicationContext context: Context): ValidateUserEmail =
        ValidateUserEmail(context)

    @Provides
    fun provideValidationUserPassword(@ApplicationContext context: Context): ValidateUserPassword =
        ValidateUserPassword(context)

    @Provides
    fun provideValidationUserRepeatedPassword(@ApplicationContext context: Context): ValidateUserRepeatedPassword =
        ValidateUserRepeatedPassword(context)

    @Provides
    fun provideValidateUserNotFound(@ApplicationContext context: Context): ValidateUserNotFound =
        ValidateUserNotFound(context)

    @Provides
    fun provideValidationUserAlreadyExist(@ApplicationContext context: Context): ValidateUserAlreadyExist =
        ValidateUserAlreadyExist(context)

    @Provides
    fun provideValidationUserRecoveryCode(@ApplicationContext context: Context): ValidateUserRecoveryCode =
        ValidateUserRecoveryCode(context)

    @Provides
    fun provideValidationUserRecoveryCodeNotExist(@ApplicationContext context: Context): ValidateUserRecoveryCodeNotExist =
        ValidateUserRecoveryCodeNotExist(context)

    @Provides
    fun provideValidationTerms(@ApplicationContext context: Context): ValidateTerms =
        ValidateTerms(context)
}