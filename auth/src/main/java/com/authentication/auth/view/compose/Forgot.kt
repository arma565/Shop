package com.authentication.auth.view.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.authentication.auth.R
import com.authentication.auth.ui.theme.Black
import com.authentication.auth.ui.theme.Blue
import com.authentication.auth.ui.theme.White
import com.authentication.auth.view.activity.AuthViewModelInstance.authenticationViewModel
import com.authentication.auth.viewmodel.AuthenticationViewModel
import com.authentication.auth.viewmodel.UserEvent

@Composable
fun ForgotCompose(
    onRecoverUserClick: (id: Int) -> Unit,
    onSignUpClick: () -> Unit
) {

    val snackBarHost = remember { SnackbarHostState() }
    var showSnack by remember { mutableStateOf(false) }
    val userValidationState = authenticationViewModel.personState
    val context = LocalContext.current
    LaunchedEffect(key1 = userValidationState) {
        authenticationViewModel.validationEvent.collect { event ->
            if (event == AuthenticationViewModel.ValidationEvent.Success) {
                onRecoverUserClick(authenticationViewModel.getUserList.value.first { it.recoveryCode == userValidationState.recoveryCode }.id)
            }
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = 20.dp),
                hostState = snackBarHost
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), colors = CardColors(
                    containerColor = White,
                    contentColor = Black,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Gray
                ), elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (showSnack && userValidationState.recoveryCodeNotExistError.isNotEmpty()) {
                        LaunchedEffect(key1 = snackBarHost) {
                            snackBarHost.showSnackbar(
                                userValidationState.recoveryCodeNotExistError,
                                actionLabel = context.getString(
                                    R.string.ok
                                )
                            )
                            showSnack = false
                            return@LaunchedEffect
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = stringResource(id = R.string.shop_image)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.reset_password),
                            color = Blue,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.please_input_your_recovery_code),
                            modifier = Modifier.padding(top = 16.dp),
                            color = Blue
                        )
                    }

                    OutlinedTextField(
                        modifier = Modifier.padding(top = 50.dp),
                        value = userValidationState.recoveryCode,
                        onValueChange = {
                            authenticationViewModel.onEvent(
                                UserEvent.RecoveryCodeChange(
                                    it
                                )
                            )
                        },

                        placeholder = {
                            Text(text = stringResource(R.string.recovery_code))
                        },
                        isError = userValidationState.recoveryCodeError.isNotEmpty(),
                        supportingText = {
                            if (userValidationState.recoveryCodeError.isNotEmpty()) {
                                Text(
                                    text = userValidationState.recoveryCodeError,
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Start
                                )
                            }
                        },
                        trailingIcon = {
                            if (userValidationState.recoveryCode.isNotEmpty()) {
                                IconButton(onClick = {
                                    authenticationViewModel.onEvent(
                                        UserEvent.RecoveryCodeChange(
                                            ""
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = stringResource(id = R.string.clear)
                                    )
                                }
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.recovery_code)) })

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 50.dp)
                    ) {
                        ExtendedFloatingActionButton(modifier = Modifier
                            .width(300.dp)
                            .wrapContentHeight()
                            .align(Alignment.TopCenter),
                            containerColor = Blue,
                            onClick = {
                                showSnack = true
                                authenticationViewModel.onEvent(UserEvent.ForgotSubmit)
                            }) {
                            Text(
                                text = stringResource(id = R.string.reset_password), color = White
                            )
                        }

                        TextButton(
                            modifier = Modifier
                                .padding(60.dp)
                                .align(Alignment.BottomEnd),
                            onClick = {
                                onSignUpClick()
                            }) {
                            Text(
                                text = "${stringResource(id = R.string.don_t_have_an_account)} ${
                                    stringResource(
                                        id = R.string.sign_up
                                    )
                                }"
                            )
                        }
                    }
                }
            }
        }
    }
}