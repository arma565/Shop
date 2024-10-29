package com.authentication.auth.viewmodel

import android.graphics.Bitmap

data class UserValidationState(
    var userName: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var currentUserPassword: String = "",
    var password: String = "",
    var repeatedPassword: String = "",
    var recoveryCode: String = "",
    var acceptedTerms: Boolean = false,
    var profilePhoto: Bitmap? = null,
    //Error
    var emailError: String = "",
    var currentUserPasswordError : String = "",
    var passwordError: String = "",
    var repeatedPasswordError: String = "",
    var userNotFoundError: String = "",
    var userAlreadyExistError: String = "",
    var recoveryCodeError: String = "",
    var recoveryCodeNotExistError: String = "",
    val acceptTermsError: String = "",
)