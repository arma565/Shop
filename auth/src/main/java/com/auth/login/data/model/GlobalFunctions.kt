package com.auth.login.data.model

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.auth.login.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object GlobalFunctions {

    fun layoutEdtEndIconMode(layout: TextInputLayout, edt: TextInputEditText) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                layout.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun logIn(activity: FragmentActivity) {
        activity.finish()
        val intentPropertyActivity =
            Intent(activity, Class.forName("com.store.shop.view.activity.ShopActivity"))
        intentPropertyActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intentPropertyActivity, activityFadeAnimation(activity as Context))
    }

    private fun activityFadeAnimation(context: Context) = ActivityOptionsCompat.makeCustomAnimation(
        context,
        android.R.anim.fade_in,
        android.R.anim.fade_out
    ).toBundle()

    fun getNavControllerFragmentAuth(activity: FragmentActivity) = (activity.supportFragmentManager.findFragmentById(R.id.fragmentContainerAuth) as NavHostFragment).navController
}