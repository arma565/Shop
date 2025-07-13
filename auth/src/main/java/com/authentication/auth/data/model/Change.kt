package com.authentication.auth.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Change(
    @SerializedName(value = "userName")
    @Expose
    val userName: String = "",
    @SerializedName(value = "currentPassword")
    @Expose
    val currentPassword: String = "",
    @SerializedName(value = "newPassword")
    @Expose
    val newPassword: String = "",
    @SerializedName(value = "repeatPassword")
    @Expose
    val repeatNewPassword: String = ""
)