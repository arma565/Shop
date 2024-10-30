package com.authentication.auth.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.authentication.auth.data.config.UserAutoLoginConfig
import com.authentication.auth.data.model.GlobalFunctions
import com.authentication.auth.data.model.GlobalFunctions.FadeInAnimation
import com.authentication.auth.data.model.GlobalFunctions.logIn
import com.authentication.auth.data.model.User
import com.authentication.auth.ui.theme.AuthTheme
import com.authentication.auth.view.activity.AuthViewModelInstance.authenticationViewModel
import com.authentication.auth.view.activity.Constants.FORGOT
import com.authentication.auth.view.activity.Constants.LOGIN
import com.authentication.auth.view.activity.Constants.RECOVERY
import com.authentication.auth.view.activity.Constants.REGISTER
import com.authentication.auth.view.compose.ForgotCompose
import com.authentication.auth.view.compose.LoginCompose
import com.authentication.auth.view.compose.RecoveryUserCompose
import com.authentication.auth.view.compose.RegisterCompose
import com.authentication.auth.viewmodel.AuthNetworkViewModel
import com.authentication.auth.viewmodel.AuthenticationViewModel
import com.authentication.auth.viewmodel.UserValidationState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val authNetworkViewModel: AuthNetworkViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalFunctions.checkNetwork(this@AuthActivity)
        if (UserAutoLoginConfig(applicationContext).getRememberCheck()) {
            logIn(this@AuthActivity)
        }
        enableEdgeToEdge()
        setContent {
            AuthTheme {
                AuthViewModelInstance.authenticationViewModel = authenticationViewModel
                AuthViewModelInstance.authNetworkViewModel = authNetworkViewModel
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = LOGIN) {
                    composable(LOGIN) {
                        FadeInAnimation {
                            LoginCompose(
                                activity = this@AuthActivity,
                                onForgotClick = {
                                    clearCache()
                                    navController.navigate(FORGOT)
                                },
                                onLoginClick = {
                                    clearCache()
                                    logIn(this@AuthActivity)
                                },
                                onSignUpClick = {
                                    clearCache()
                                    navController.navigate(REGISTER)
                                })
                        }
                    }
                    composable(REGISTER) {
                        FadeInAnimation {
                            RegisterCompose(
                                activity = this@AuthActivity
                            ) {
                                clearCache()
                                navController.navigate(LOGIN)
                            }
                        }
                    }
                    composable(FORGOT) {
                        FadeInAnimation {
                            ForgotCompose(
                                onRecoverUserClick = {
                                    clearCache()
                                    navController.navigate("$RECOVERY/$it")
                                },
                                onSignUpClick = {
                                    clearCache()
                                    navController.navigate(REGISTER)
                                }
                            )
                        }
                    }

                    composable(
                        route = "$RECOVERY/{id}", arguments = listOf(navArgument("id") {
                            type = NavType.IntType
                        })
                    ) { navBackStackEntry ->
                        val userId = navBackStackEntry.arguments?.getInt("id")
                        val user: User = authenticationViewModel.getSpecificUser(userId!!)
                        if (user != User()) {
                            FadeInAnimation {
                                RecoveryUserCompose(user = user) {
                                    clearCache()
                                    navController.navigate(LOGIN)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun clearCache() {
    authenticationViewModel.personState = UserValidationState()
}

object AuthViewModelInstance {
    lateinit var authenticationViewModel: AuthenticationViewModel
    lateinit var authNetworkViewModel: AuthNetworkViewModel
}

object Constants {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT = "forgot"
    const val RECOVERY = "recovery"
}