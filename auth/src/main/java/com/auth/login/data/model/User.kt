package com.auth.login.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * User entity(model)
 */
@Parcelize
@Entity(tableName = "tbl_user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo("username")
    var username: String? = "",
    @ColumnInfo("firstName")
    var firstName: String? = "",
    @ColumnInfo("lastName")
    var lastName: String? = "",
    @ColumnInfo("phoneNumber")
    var phoneNumber: String? = "",
    @ColumnInfo("email")
    var email: String? = "",
    @ColumnInfo("password")
    var password: String? = "",
    @ColumnInfo("recovery")
    var recoveryCode: String? = "0",
    var confirm: String? = "",
    @ColumnInfo("photo")
    var profilePhoto: Bitmap? = null
) : Parcelable