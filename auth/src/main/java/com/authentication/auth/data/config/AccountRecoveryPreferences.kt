package com.authentication.auth.data.config

import android.content.Context
import androidx.core.content.edit

class AccountRecoveryPreferences(context : Context) {
    companion object{
        private const val RECOVERY_SP = "RecoverySP"
        private const val EMAIL_KEY = "email"
        private const val TOKEN_KEY = "token"
    }

    private val sp = context.getSharedPreferences(RECOVERY_SP,Context.MODE_PRIVATE)

    fun set(email : String , token : String){
        sp.edit().apply {
            this.putString(EMAIL_KEY,email)
            this.putString(TOKEN_KEY,token)
            this.commit()
        }
    }

    fun getEmail() : String?{
        return sp.getString(EMAIL_KEY,"")
    }

    fun getToken() : String?{
        return sp.getString(TOKEN_KEY,"")
    }

    fun clearAll() {
        sp.edit { clear() }
    }
}