package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R

class ValidateLastName(var context: Context) {
    fun execute(lastName: String): ValidationResult {
        if (lastName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.last_name_required)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}