package com.auth.login.data.repositories

import com.auth.login.data.local.IAuthDao
import com.auth.login.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao : IAuthDao
) {
    suspend fun upsertUser(user: User) = dao.upsertUser(user)
    fun userList() : Flow<List<User>> = flow {
        this.emit(dao.usersListDesc())
        delay(1000L)
    }

//    suspend fun deleteAllUsers() = dao.deleteAllUsers()
    suspend fun deleteUser(user: User) = dao.deleteUser(user)
}