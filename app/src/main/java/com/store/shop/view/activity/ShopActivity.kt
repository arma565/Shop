package com.store.shop.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.auth.login.view.activity.SettingActivity
import com.store.shop.R
import com.store.shop.data.model.GlobalFunctions
import com.store.shop.databinding.ActivityShopBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)

        (this@ShopActivity as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.shop_menu, menu)

                menu.findItem(R.id.item_settings).setOnMenuItemClickListener {
                    this@ShopActivity.finish()
                    val intent = Intent(applicationContext, SettingActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(
                        intent, ActivityOptionsCompat.makeCustomAnimation(
                            applicationContext,
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        ).toBundle()
                    )
                    true
                }


            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        })

        val navigation =
            GlobalFunctions.getNavControllerActivity(binding.shopContainerView)

        navigation.navigate(R.id.action_global_homeFragment)

        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    navigation.navigate(R.id.action_global_homeFragment)
                    binding.bottomNavi.menu.findItem(R.id.item_home)
                        .setChecked(true)
                }

                R.id.item_category -> {
                    navigation.navigate(R.id.action_global_categoryFragment)
                    binding.bottomNavi.menu.findItem(R.id.item_category)
                        .setChecked(true)
                }

                R.id.item_favorite -> {
                    navigation.navigate(R.id.action_global_favoriteFragment)
                    binding.bottomNavi.menu.findItem(R.id.item_favorite)
                        .setChecked(true)
                }
            }
            true
        }
    }
}