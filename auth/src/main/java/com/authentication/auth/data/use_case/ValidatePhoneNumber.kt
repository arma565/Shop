package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R

class ValidatePhoneNumber(var context: Context) {
    fun execute(phoneNumber: String): ValidationResult {
        if (phoneNumber.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.phone_number_required)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}