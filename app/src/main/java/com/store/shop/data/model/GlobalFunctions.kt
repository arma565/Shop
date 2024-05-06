package com.store.shop.data.model

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.store.shop.R
import com.store.shop.view.activity.MainActivity

object GlobalFunctions {

    /**
     * Return navController to activity
     */
    fun getNavControllerActivity(fragmentContainerViewProperty : FragmentContainerView) : NavController {
        val navHostFragment = fragmentContainerViewProperty.getFragment<NavHostFragment>()
        return navHostFragment.navController
    }
    /**
     * Return navController to fragment
     */
    fun getNavControllerFragment(activity : FragmentActivity) : NavController {
        val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.shopContainerView) as NavHostFragment
        return navHostFragment.navController
    }


    fun getResult(activity: AppCompatActivity) {
        activity.finish()
        val intentProperty = Intent(activity, MainActivity::class.java)
        intentProperty.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intentProperty, activityFadeAnimation(activity as Context))
    }

    private fun activityFadeAnimation(context: Context) = ActivityOptionsCompat.makeCustomAnimation(
        context,
        android.R.anim.fade_in,
        android.R.anim.fade_out
    ).toBundle()

}