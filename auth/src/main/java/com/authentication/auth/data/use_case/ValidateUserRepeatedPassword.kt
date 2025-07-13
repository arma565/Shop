package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R

class ValidateUserRepeatedPassword(var context: Context) {
    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if (repeatedPassword.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.repeated_password_empty)
            )
        }
        if (!(passwordContainsNonAlphanumeric(repeatedPassword))){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.password_alphanumeric)
            )
        }
        if (!(passwordContainsNonAlphanumeric(password))){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.password_alphanumeric)
            )
        }
        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.passwords_not_match)
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    private fun passwordContainsNonAlphanumeric(password: String): Boolean {
        // Regular expression to check for at least one non-alphanumeric character
        return password.any { !it.isLetterOrDigit() }
    }
}