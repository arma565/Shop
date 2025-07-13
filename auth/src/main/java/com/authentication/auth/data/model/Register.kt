package com.authentication.auth.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName(value = "userName")
    @Expose
    val userName : String = "",
    @SerializedName(value = "email")
    @Expose
    val email : String = "",
    @SerializedName(value = "password")
    @Expose
    val password : String = "",
    @SerializedName(value = "repeatPassword")
    @Expose
    val repeatPassword : String = "",
    @SerializedName(value = "acceptTerms")
    @Expose
    val acceptTerms : Boolean = false
)