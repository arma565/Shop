package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R

class ValidateUserRepeatedPassword(var context: Context) {
    fun execute(password : String,repeatedPassword : String): ValidationResult {
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
}