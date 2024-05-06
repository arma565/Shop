package com.auth.login.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.auth.login.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }
}