package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R

class ValidateUserName(var context: Context) {
    fun execute(userName: String): ValidationResult {
        if (userName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.user_name_required)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}