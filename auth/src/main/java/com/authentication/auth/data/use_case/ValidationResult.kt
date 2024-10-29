package com.authentication.auth.data.use_case

data class ValidationResult(
    val successful : Boolean,
    val errorMessage : String = ""
)
