package com.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.login.data.model.User
import com.auth.login.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Upsert user in DB
     * @param user: user model
     */
    fun upsertUser(user: User) {
        viewModelScope.launch(IO) {
            userRepository.upsertUser(user)
        }
    }

    /**
     * Get user list from db
     */
    fun getUserList(): Flow<List<User>> = flow {
        userRepository.userList().collect {
            this.emit(it)
            delay(1000L)
        }
    }

    /**
     * Delete user
     * @param user: User model
     */
    fun deleteUser(user: User) {
        viewModelScope.launch(IO) {
            userRepository.deleteUser(user)
        }
    }
}