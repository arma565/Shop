package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R
import com.authentication.auth.data.model.User

class ValidateUserNotFound(var context: Context) {
    fun execute(userInDb: User, user: User): ValidationResult {
        if (checkInput(userInDb, user)) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.user_not_found)
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    private fun checkInput(
        userInDb: User,
        user: User
    ) =  userInDb.email != user.email || userInDb.password != user.password
}