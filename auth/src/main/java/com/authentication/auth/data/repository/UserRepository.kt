package com.authentication.auth.data.repository

import com.authentication.auth.data.local.IUserDao
import com.authentication.auth.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class UserRepository @Inject constructor(
    private val dao: IUserDao
) : IUserDao {
    override suspend fun upsertUser(user: User) = dao.upsertUser(user)

    override fun usersListDesc(): Flow<List<User>> = dao.usersListDesc()

    override suspend fun deleteUser(user: User) = dao.deleteUser(user)
}