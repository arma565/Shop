package com.authentication.auth.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User entity(model)
 */
@Entity(tableName = "tbl_user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo("user_name")
    var username: String = "",
    @ColumnInfo("first_name")
    var firstName: String = "",
    @ColumnInfo("last_name")
    var lastName: String = "",
    @ColumnInfo("phone_number")
    var phoneNumber: String = "",
    var email: String = "",
    var password: String = "",
    @ColumnInfo("repeated_password")
    var repeatedPassword: String = "",
    @ColumnInfo("recovery_code")
    var recoveryCode: String = "",
    @ColumnInfo("accept_terms")
    var acceptTerms: Boolean = false,
    @ColumnInfo("profile_photo")
    var profilePhoto: Bitmap? = null
)