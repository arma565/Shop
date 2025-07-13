package com.authentication.auth.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Recovery(
    @SerializedName(value = "email")
    @Expose
    val email : String = "",
)