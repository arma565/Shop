package com.store.shop.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.store.shop.R
import com.store.shop.data.model.GlobalFunctions
import com.store.shop.databinding.ActivityMainBinding
import com.store.shop.databinding.ActivityShopBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = GlobalFunctions.getNavControllerActivity(binding.shopContainerView)

        navigation.navigate(R.id.action_global_homeFragment)

        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    navigation.navigate(R.id.action_global_homeFragment)
                    binding.bottomNavi.menu.findItem(R.id.item_home).setChecked(true)
                }

                R.id.item_category -> {
                    navigation.navigate(R.id.action_global_categoryFragment)
                    binding.bottomNavi.menu.findItem(R.id.item_category).setChecked(true)
                }

                R.id.item_setting -> {
                    navigation.navigate(R.id.action_global_settingFragment)
                    binding.bottomNavi.menu.findItem(R.id.item_setting).setChecked(true)
                }
            }
            true
        }
    }
}