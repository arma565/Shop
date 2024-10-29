package com.authentication.auth.data.use_case

import android.content.Context
import android.util.Patterns
import com.authentication.auth.R

class ValidateUserEmail(var context: Context) {
    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.email_require)
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !checkLocale(email)) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.email_not_valid)
            )
        }
        return ValidationResult(
            successful = true
        )
    }

  private fun checkLocale(input : String) : Boolean{
      var isEnglish = true
      for ( c in input.toCharArray() ) {
          if ( Character.UnicodeBlock.of(c) != Character.UnicodeBlock.BASIC_LATIN ) {
              isEnglish = false
              break
          }
      }
      return isEnglish
  }
}