package com.authentication.auth.data.model

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityOptionsCompat
import com.authentication.auth.data.config.UserAutoLoginConfig
import com.authentication.auth.view.activity.AuthActivity
import com.network.state.IResponseEvent
import com.network.state.NetworkStateManager
import kotlinx.coroutines.delay

object GlobalFunctions {

    fun logIn(activity: ComponentActivity) {
        activity.finish()
        val intentPropertyActivity = Intent(activity, Class.forName("com.store.shop.view.activity.ShopActivity"))
        intentPropertyActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intentPropertyActivity, activityFadeAnimation(activity as Context))
    }

    fun logOut(activity: ComponentActivity) {
        activity.finish()
        val intentAccountToProperty = Intent(activity, AuthActivity::class.java)
        intentAccountToProperty.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intentAccountToProperty, activityFadeAnimation(activity as Context))
        val userAutoLoginConfig = UserAutoLoginConfig(activity)
        userAutoLoginConfig.clearAll()
    }

    private fun activityFadeAnimation(context: Context) = ActivityOptionsCompat.makeCustomAnimation(
        context,
        android.R.anim.fade_in,
        android.R.anim.fade_out
    ).toBundle()

    @Composable
    fun FadeInAnimation(content: @Composable () -> Unit) {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = Unit, block = {
            delay(300L)
            visible = true
        })
        AnimatedVisibility(
            visible = visible,
            modifier = Modifier.fillMaxSize(),
            enter = fadeIn(
                initialAlpha = 0.0f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            content()
        }
    }

    fun checkNetwork(activity: ComponentActivity) {
        try {
            NetworkStateManager(activity, activity).start(object :
                IResponseEvent {
                override fun state(state: Boolean) {
                    if (!state) {
                        Toast.makeText(
                            activity, "No internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun serverState(state: Boolean) {
                    if (!state) {
                        Toast.makeText(
                            activity, "Server is not available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } catch (e: Exception) {
            Toast.makeText(activity, "Unknown system error! $e", Toast.LENGTH_LONG).show()
        }
    }
}