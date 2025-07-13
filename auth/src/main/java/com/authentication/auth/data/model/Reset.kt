package com.authentication.auth.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Reset(
    @SerializedName(value = "email")
    @Expose
    val email: String = "",
    @SerializedName(value = "token")
    @Expose
    val token: String = "",
    @SerializedName(value = "newPassword")
    @Expose
    val newPassword: String = "",
    @SerializedName(value = "repeatNewPassword")
    @Expose
    val repeatNewPassword: String = ""
)