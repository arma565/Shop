package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R

class ValidateUserRecoveryCode(var context: Context) {
    fun execute(recoveryCode : String): ValidationResult {
        if (recoveryCode.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.recovery_code_is_require)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}