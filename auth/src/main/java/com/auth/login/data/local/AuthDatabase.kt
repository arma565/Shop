package com.auth.login.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.auth.login.data.model.ImageConverter
import com.auth.login.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(ImageConverter::class)
abstract class AuthDatabase : RoomDatabase() {
    abstract fun dao() : IAuthDao
}