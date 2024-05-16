package com.auth.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.login.data.repositories.AuthNetworkRepository
import com.google.gson.internal.LinkedTreeMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthNetworkViewModel @Inject constructor(
    private val authNetworkRepository: AuthNetworkRepository
) : ViewModel() {
    private lateinit var loginLiveData: MutableLiveData<String>
    private lateinit var registerLiveData: MutableLiveData<Double>

    /**
     * register
     */
    fun register(username: String, password: String): LiveData<Double> {
        registerLiveData = MutableLiveData()
        viewModelScope.launch(IO) {
            registerLiveData.postValue(
                (authNetworkRepository.register(username, password)
                    .body() as LinkedTreeMap<*, *>)["code"] as Double
            )
        }
        return registerLiveData
    }


    /**
     * login
     */
    fun login(username: String, password: String): LiveData<String> {
        loginLiveData = MutableLiveData()
        viewModelScope.launch(IO) {
            loginLiveData.postValue(
                (authNetworkRepository.login(username, password)
                    .body() as LinkedTreeMap<*, *>)["code"] as String
            )
        }
        return loginLiveData
    }

}