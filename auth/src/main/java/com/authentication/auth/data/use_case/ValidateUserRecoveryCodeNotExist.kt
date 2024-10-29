package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R

class ValidateUserRecoveryCodeNotExist(var context: Context) {
    fun execute(recoveryCodeInDb : String, recoveryCode : String): ValidationResult {
        if (recoveryCode.isNotEmpty() && recoveryCodeInDb != recoveryCode) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.recovery_code_is_incorrect)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}