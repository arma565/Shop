package com.auth.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.login.data.repositories.AuthNetworkRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthNetworkViewModel @Inject constructor(
    private val authNetworkRepository: AuthNetworkRepository
) : ViewModel() {
    private lateinit var loginLiveData: MutableLiveData<String>
    private lateinit var registerLiveData: MutableLiveData<Int>

    /**
     * login
     */
    fun login(username: String, password: String): LiveData<String> {
        loginLiveData = MutableLiveData()
        viewModelScope.launch {
            authNetworkRepository.login(username, password).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    loginLiveData.postValue(response.body()?.get("code")?.asString)
                }

                override fun onFailure(p0: Call<JsonObject>, p1: Throwable) {
                    loginLiveData.postValue("0")
                }

            })
        }
        return loginLiveData
    }

    /**
     * register
     */
    fun register(username: String, password: String): LiveData<Int> {
        registerLiveData = MutableLiveData()
        authNetworkRepository.register(username, password).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                registerLiveData.postValue(response.body()?.get("code")?.asInt)
            }

            override fun onFailure(response: Call<JsonObject>, p1: Throwable) {
                registerLiveData.postValue(0)
            }

        })
        return registerLiveData
    }
}