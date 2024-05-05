package com.auth.login.data.repositories

import com.auth.login.data.remote.AuthApiService
import com.google.gson.JsonObject
import retrofit2.Call
import javax.inject.Inject

class AuthNetworkRepository @Inject constructor(
    private val apiService: AuthApiService
) {

    fun login(username: String, password: String): Call<JsonObject> = apiService.login(username, password)

    fun register(username: String, password: String): Call<JsonObject> = apiService.register(username, password)

}