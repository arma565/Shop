package com.store.shop.data.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class FavoriteProductConfig(context: Context) {

    private val sp: SharedPreferences =
        context.getSharedPreferences("FaveConfig", Context.MODE_PRIVATE)


    fun save(favoriteProductCode: Int) {
        sp.edit {
            this.putInt("data", favoriteProductCode)
            this.apply()
        }
    }

    fun getFaveCode(): Int = sp.getInt("data", 0)
}