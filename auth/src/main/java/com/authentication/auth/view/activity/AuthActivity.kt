package com.authentication.auth.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.authentication.auth.R
import com.authentication.auth.data.model.GlobalFunctions.FadeAnimation
import com.authentication.auth.data.model.GlobalFunctions.logIn
import com.authentication.auth.other.Constants.FORGOT
import com.authentication.auth.other.Constants.LOGIN
import com.authentication.auth.other.Constants.RECOVERY
import com.authentication.auth.other.Constants.REGISTER
import com.authentication.auth.ui.theme.AuthTheme
import com.authentication.auth.view.compose.ForgotCompose
import com.authentication.auth.view.compose.LoginCompose
import com.authentication.auth.view.compose.RecoveryUserCompose
import com.authentication.auth.view.compose.RegisterCompose
import com.authentication.auth.viewmodel.AuthViewModel
import com.network.state.viewmodel.NetworkStatusViewModel
import dagger.hilt.android.AndroidEntryPoint

//TODO Add a button to refresh server not responding screen
@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val networkStatusViewModel: NetworkStatusViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val isConnected by networkStatusViewModel.isConnected.collectAsState()
            val isServerResponding by networkStatusViewModel.isServerResponding.collectAsState()

            LaunchedEffect(true) {
                networkStatusViewModel.start(this@AuthActivity)
            }

            AuthTheme {
                when (true) {
                    !isConnected -> NoNetworkScreen()
                    !isServerResponding -> ServerNotRespondingScreen()
                    else -> AuthNavigation(authViewModel)
                }
            }
        }
    }

    @Composable
    private fun AuthNavigation(authViewModel: AuthViewModel) {
        if (authViewModel.getRememberCheckFromSharedPreferences()) {
            LaunchedEffect(Unit) {
                logIn(this@AuthActivity)
            }
        }
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = LOGIN) {
            composable(LOGIN) {
                FadeAnimation {
                    LoginCompose(
                        activity = this@AuthActivity,
                        networkStatusViewModel = networkStatusViewModel,
                        authViewModel = authViewModel,
                        onForgotClick = {
                            navController.navigate(FORGOT)
                        },
                        onLoginClick = {
                            logIn(this@AuthActivity)
                        },
                        onSignUpClick = {
                            navController.navigate(REGISTER)
                        })
                }
            }
            composable(REGISTER) {
                FadeAnimation {
                    RegisterCompose(
                        activity = this@AuthActivity,
                        networkStatusViewModel = networkStatusViewModel,
                        authViewModel = authViewModel
                    ) {
                        navController.navigate(LOGIN)
                    }
                }
            }
            composable(FORGOT) {
                FadeAnimation {
                    ForgotCompose(
                        activity = this@AuthActivity,
                        networkStatusViewModel = networkStatusViewModel,
                        authViewModel = authViewModel,
                        onRecoverUserClick = {
                            navController.navigate(RECOVERY)
                        },
                        onSignUpClick = {
                            navController.navigate(REGISTER)
                        }
                    )
                }
            }
            composable(RECOVERY) {
                FadeAnimation {
                    RecoveryUserCompose(
                        activity = this@AuthActivity,
                        networkStatusViewModel = networkStatusViewModel,
                        authViewModel = authViewModel
                    ) {
                        navController.navigate(LOGIN)
                    }
                }
            }
        }
    }

    @Composable
    fun NoNetworkScreen() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.network_problem))
        }
    }

    @Composable
    fun ServerNotRespondingScreen() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.server_not_response))
        }
    }
}



