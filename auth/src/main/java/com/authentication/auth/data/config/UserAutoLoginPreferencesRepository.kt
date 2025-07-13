package com.authentication.auth.data.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


/**
 * This will use if user check remember me(CheckBox)
 */
class UserAutoLoginPreferencesRepository(context: Context) {

    companion object {
        private const val SP_USER_AUTO_LOGIN_CONFIG = "userAutoLogin"
        private const val USER_NAME_KEY = "user_name"
        private const val REMEMBER_CHECK_USER_LOGIN_KEY = "remember_check"
    }

    /**
     * Define shared preferences and activity
     */
    private var sp: SharedPreferences =
        context.getSharedPreferences(SP_USER_AUTO_LOGIN_CONFIG, Context.MODE_PRIVATE)

    /**
     * Save to shared preferences
     */
    fun save(userName: String, isRememberCheck: Boolean) {
        sp.edit {
            this.putString(USER_NAME_KEY, userName)
            this.putBoolean(REMEMBER_CHECK_USER_LOGIN_KEY, isRememberCheck)
            this.commit()
        }
    }

    /**
     * Get user id from shared preferences
     */
    fun getUserName(): String? {
        return sp.getString(USER_NAME_KEY, "")
    }

    /**
     * Get user login state from shared preferences(if user check remember me)
     */
    fun getRememberCheck(): Boolean {
        return sp.getBoolean(REMEMBER_CHECK_USER_LOGIN_KEY, false)
    }

    /**
     * Clear shared preferences
     */
    fun clearAll() {
        sp.edit { clear() }
    }
}