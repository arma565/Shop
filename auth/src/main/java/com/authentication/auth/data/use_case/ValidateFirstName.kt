package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R

class ValidateFirstName(var context: Context) {
    fun execute(firstName: String): ValidationResult {
        if (firstName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.first_name_required)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}