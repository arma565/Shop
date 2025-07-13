package com.authentication.auth.viewmodel

data class UserValidationState(
    var userName: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var token : String = "",
    var currentUserPassword: String = "",
    var password: String = "",
    var repeatedPassword: String = "",
    var acceptedTerms: Boolean = false,
    //Error
    var userNameError: String = "",
    var firstNameError: String = "",
    var lastNameError: String = "",
    var phoneNumberError: String = "",
    var emailError: String = "",
    var currentUserPasswordError: String = "",
    var passwordError: String = "",
    var repeatedPasswordError: String = "",
    val acceptTermsError: String = "",
)