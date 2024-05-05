package com.auth.login.data.local.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.auth.login.R
import com.auth.login.data.model.User

/**
 * This is used for save user login info
 */
class UserInfoConfig(private val context: Context) {
    /**
     * Define shared preferences and activity
     */
    private var sp: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.user), Context.MODE_PRIVATE)

    /**
     * Save to shared preferences
     */
    fun save(user: User) {
        sp.edit {
            this.putString(context.getString(R.string.email), user.email)
            this.putInt(context.getString(R.string.userid), user.id)
            this.commit()
        }
    }

    /**
     * Get email from shared preferences
     */
    fun getEmail(): String? {
        return sp.getString(context.getString(R.string.email), "")
    }

    /**
     * Clear shared preferences
     */
    fun clearAll() {
        sp.edit().clear().apply()
    }

}