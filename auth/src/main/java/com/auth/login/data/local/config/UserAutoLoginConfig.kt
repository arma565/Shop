package com.auth.login.data.local.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.auth.login.R
import com.auth.login.data.model.User


/**
 * This will use if user check remember me(CheckBox)
 */
class UserAutoLoginConfig(private val context: Context) {

    /**
     * Define shared preferences and activity
     */
    private var sp: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.user_auto_login),
        Context.MODE_PRIVATE
    )

    /**
     * Save to shared preferences
     */
    fun save(user: User) {
        sp.edit {
            this.putString(context.getString(R.string.email), user.email)
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