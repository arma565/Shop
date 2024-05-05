package com.auth.login.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.auth.login.databinding.ActivityLoginMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityLoginMainBinding = ActivityLoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}