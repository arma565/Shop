package com.authentication.auth.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * User entity(model)
 */
data class User(
    @SerializedName(value = "$/id")
    var id: Int = 0,
    @SerializedName("profileImagePath")
    @Expose
    var profileImagePath: String = "",
    @SerializedName("firstName")
    @Expose
    var firstName: String = "",
    @SerializedName("lastName")
    @Expose
    var lastName: String = "",
    @SerializedName("acceptTerms")
    @Expose
    var acceptTerms: Boolean = false,
    @SerializedName("userName")
    @Expose
    var username: String = "",
    @SerializedName("email")
    @Expose
    var email: String = "",
    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: String = "",
)

