package com.authentication.auth.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.authentication.auth.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface IUserDao {

    /**
     *  Create or update User
     */
    @Upsert
    suspend fun upsertUser(user: User)

    /**
     * List of users
     */
    @Query("SELECT * FROM tbl_user ORDER BY id DESC")
    fun usersListDesc(): Flow<List<User>>

    /**
     * Delete a user from database
     */
    @Delete
    suspend fun deleteUser(user: User)
}