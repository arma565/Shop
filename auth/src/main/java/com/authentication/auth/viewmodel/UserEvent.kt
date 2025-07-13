package com.authentication.auth.viewmodel

sealed class UserEvent {
    data class UserNameChanged(val userName: String) : UserEvent()
    data class FirstNameChanged(val firstName: String) : UserEvent()
    data class LastNameChanged(val lastName: String) : UserEvent()
    data class PhoneNumberChangedChanged(val phoneNumber: String) : UserEvent()
    data class EmailChanged(val email: String) : UserEvent()
    data class TokenChanged(val token: String) : UserEvent()
    data class CurrentUserPasswordChanged(val currentUserPasswordChanged: String) : UserEvent()
    data class PasswordChanged(val password: String) : UserEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String) : UserEvent()
    data class AcceptTermsChanged(val isAccepted: Boolean) : UserEvent()
    data object ForgotSubmit : UserEvent()
    data object LoginSubmit : UserEvent()
    data object RecoverySubmit : UserEvent()
    data object RegisterSubmit : UserEvent()
    data object UpdateProfileSubmit : UserEvent()
}