package com.authentication.auth.data.use_case

import android.content.Context
import com.authentication.auth.R
import com.authentication.auth.data.model.User

class ValidateUserAlreadyExist(var context: Context) {
    fun execute(userInDb: User, user: User): ValidationResult {
        if (checkExistence(user, userInDb)) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.user_already_exist)
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    private fun checkExistence(user: User, userInDB: User) = user.email == userInDB.email && user.password == userInDB.password
}