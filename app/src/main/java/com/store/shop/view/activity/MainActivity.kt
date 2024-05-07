package com.store.shop.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.auth.login.view.activity.LoginMainActivity
import com.store.shop.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         this.finish()
         startActivity(
             Intent(applicationContext, LoginMainActivity::class.java),
             ActivityOptionsCompat.makeCustomAnimation(
                 applicationContext,
                 android.R.anim.fade_in,
                 android.R.anim.fade_out
             ).toBundle()
         )
    }
}