package com.authentication.auth.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName(value = "UserName")
    @Expose
    val userName: String = "",
    @SerializedName(value = "Password")
    @Expose
    val password: String = "")