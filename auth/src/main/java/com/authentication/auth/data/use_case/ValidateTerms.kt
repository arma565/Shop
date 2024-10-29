package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R


class ValidateTerms(var context: Context) {
    fun execute(acceptedTerms : Boolean): ValidationResult {
        if (!acceptedTerms) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.terms_require)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}