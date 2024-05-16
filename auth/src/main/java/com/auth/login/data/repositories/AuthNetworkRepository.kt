package com.auth.login.data.repositories

import com.auth.login.data.remote.AuthApiService
import com.google.gson.JsonObject
import retrofit2.Response
import javax.inject.Inject

class AuthNetworkRepository @Inject constructor(
    private val apiService: AuthApiService
) {

    suspend fun login(username: String, password: String): Response<Any> =
        apiService.login(username, password)

    suspend fun register(username: String, password: String): Response<Any> =
        apiService.register(username, password)

}