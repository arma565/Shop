package com.store.shop.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.auth.login.view.activity.LoginMainActivity
import com.auth.login.viewmodel.AuthenticationViewModel
import com.store.shop.R
import com.store.shop.data.model.GlobalFunctions
import com.store.shop.databinding.ActivityMainBinding
import com.store.shop.viewmodel.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.finish()
        startActivity(Intent(applicationContext,LoginMainActivity::class.java))



    }
}