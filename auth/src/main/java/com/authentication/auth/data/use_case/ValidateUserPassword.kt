package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R

class ValidateUserPassword(var context: Context) {
    fun execute(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.password_require)
            )
        }
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.password_length)
            )
        }
        val passwordContainsLetterAndDigits = password.any { it.isDigit() } && password.any { it.isLetter() }
        if (!passwordContainsLetterAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.password_digit_letter)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}