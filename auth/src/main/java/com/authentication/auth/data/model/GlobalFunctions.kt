package com.authentication.auth.data.model

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityOptionsCompat
import com.authentication.auth.data.config.UserAutoLoginPreferencesRepository
import com.authentication.auth.view.activity.AuthActivity
import kotlinx.coroutines.delay

object GlobalFunctions {

    fun logIn(activity: ComponentActivity) {
        activity.finish()
        val intentPropertyActivity =
            Intent(activity, Class.forName("com.store.shop.view.activity.ShopActivity"))
        intentPropertyActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intentPropertyActivity, activityFadeAnimation(activity as Context))
    }

    fun logOut(activity: ComponentActivity) {
        activity.finish()
        val intentAccountToProperty = Intent(activity, AuthActivity::class.java)
        intentAccountToProperty.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intentAccountToProperty, activityFadeAnimation(activity as Context))
        val userAutoLoginPreferencesRepository = UserAutoLoginPreferencesRepository(activity)
        userAutoLoginPreferencesRepository.clearAll()
    }

    private fun activityFadeAnimation(context: Context) = ActivityOptionsCompat.makeCustomAnimation(
        context,
        android.R.anim.fade_in,
        android.R.anim.fade_out
    ).toBundle()

    @Composable
    fun FadeAnimation(
        modifier: Modifier = Modifier,
        delayMillis: Long = 300L,
        durationMillis: Int = 300,
        easing : Easing = FastOutSlowInEasing,
        content: @Composable () -> Unit
    ) {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = Unit, block = {
            delay(delayMillis)
            visible = true
        })
        AnimatedVisibility(
            visible = visible,
            modifier = modifier.fillMaxSize(),
            enter = fadeIn(
                initialAlpha = 0.0f,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = easing
                )
            ), exit = fadeOut(
                targetAlpha = 0.0f,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = easing
                )
            ), label = ""
        ) {
            content()
        }
    }
}