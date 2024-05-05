package com.auth.login.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.auth.login.data.model.User

@Dao
interface IAuthDao {

    /**
     *  Create User
     */
    @Upsert
    suspend fun upsertUser(user: User)

    /**
     * List of users
     */
    @Query("SELECT * FROM tbl_user ORDER BY id DESC")
    fun usersListDesc(): List<User>

    /**
     * Delete All
     */
    @Query("DELETE FROM tbl_user")
    suspend fun deleteAllUsers()

    /**
     * Delete a user from database
     */
    @Delete
    suspend fun deleteUser(user: User)
}