package com.authentication.auth.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.authentication.auth.data.model.ImageConverter
import com.authentication.auth.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(ImageConverter::class)
abstract class
UserDatabase : RoomDatabase() {
    abstract fun userDao() : IUserDao
}