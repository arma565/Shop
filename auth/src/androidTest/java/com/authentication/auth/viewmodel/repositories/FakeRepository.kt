package com.authentication.auth.viewmodel.repositories

import com.authentication.auth.data.local.IUserDao
import com.authentication.auth.data.model.User
import com.authentication.auth.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class FakeRepository(private val dao : IUserDao) : UserRepository(dao) {
    override suspend fun upsertUser(user: User) = dao.upsertUser(user)

    override fun usersListDesc(): Flow<List<User>> = dao.usersListDesc()

    override suspend fun deleteUser(user: User) = dao.deleteUser(user)
}