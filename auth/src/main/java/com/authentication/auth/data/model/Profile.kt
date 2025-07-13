package com.authentication.auth.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Profile (
    @SerializedName(value = "userName")
    @Expose
    val userName : String = "",
    @SerializedName(value = "firstName")
    @Expose
    val firstName : String = "",
    @SerializedName(value = "lastName")
    @Expose
    val lastName : String = "",
    @SerializedName(value = "phoneNumber")
    @Expose
    val phoneNumber : String = "",
)