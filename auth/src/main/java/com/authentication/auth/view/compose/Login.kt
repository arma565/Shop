package com.authentication.auth.view.compose

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.authentication.auth.R
import com.authentication.auth.data.model.Login
import com.authentication.auth.ui.theme.Black
import com.authentication.auth.ui.theme.Blue
import com.authentication.auth.ui.theme.White
import com.authentication.auth.viewmodel.AuthViewModel
import com.network.state.viewmodel.NetworkStatusViewModel
import com.authentication.auth.viewmodel.UserEvent


@Composable
fun LoginCompose(
    activity: ComponentActivity,
    networkStatusViewModel: NetworkStatusViewModel,
    authViewModel: AuthViewModel,
    onForgotClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val snackBarHost = remember { SnackbarHostState() }
    var rememberMeChecked by rememberSaveable { mutableStateOf(false) }
    var showSnack by remember { mutableStateOf(false) }
    var serverError by remember { mutableStateOf("") }

    val userValidationState = authViewModel.personState
    val context = LocalContext.current

    LaunchedEffect(key1 = userValidationState) {
        activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
            authViewModel.validationEvent.collect validationEvent@{ event ->
                if (event == AuthViewModel.ValidationEvent.Success) {
                    networkStatusViewModel.start(activity)
                    authViewModel.loginUser(
                        Login(
                            userName = userValidationState.userName,
                            password = userValidationState.password
                        )
                    ) { res ->
                        if (res.isSuccessful) {
                            authViewModel.saveUserInfoIntoSharedPreferences(
                                userValidationState.userName,
                                true
                            )
                            authViewModel.clearCache()
                            onLoginClick()
                        } else {
                            serverError = res.errorBody()?.string()!!
                            showSnack = true
                        }
                    }
                }
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
                    .wrapContentHeight(),
                colors = CardColors(
                    containerColor = White,
                    contentColor = Black,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Gray
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (showSnack && serverError.isNotEmpty()) {
                        LaunchedEffect(key1 = snackBarHost) {
                            snackBarHost.showSnackbar(
                                message = serverError,
                                actionLabel = context.getString(R.string.ok)
                            )
                            showSnack = false
                            return@LaunchedEffect
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.estate),
                        contentDescription = stringResource(id = R.string.estate_image)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Text(
                            text = stringResource(id = R.string.welcome),
                            color = Blue,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.login),
                            modifier = Modifier.padding(top = 16.dp),
                            color = Blue
                        )
                    }


                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    LoginComposeTextFields(
                        valueSet = userValidationState.userName,
                        onValueChangeSet = {
                            authViewModel.onEvent(UserEvent.UserNameChanged(it))
                        },
                        onPlaceHolderId = R.string.user_name,
                        keyboardType = KeyboardType.Text,
                        isError = userValidationState.userNameError.isNotEmpty(),
                        errorMessage = userValidationState.userNameError,
                        onTrailingIconSet = {
                            authViewModel.onEvent(UserEvent.UserNameChanged(""))
                        },
                        labelId = R.string.user_name
                    )

                    LoginComposeTextFields(
                        valueSet = userValidationState.password,
                        onValueChangeSet = {
                            authViewModel.onEvent(
                                UserEvent.PasswordChanged(
                                    it
                                )
                            )
                        },
                        onPlaceHolderId = R.string.password,
                        keyboardType = KeyboardType.Password,
                        isVisualAllowed = true,
                        isError = userValidationState.passwordError.isNotEmpty(),
                        errorMessage = userValidationState.passwordError,
                        onTrailingIconSet = {
                            authViewModel.onEvent(
                                UserEvent.PasswordChanged(
                                    ""
                                )
                            )
                        },
                        labelId = R.string.password
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .padding(start = 50.dp)
                                .align(Alignment.CenterStart),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Checkbox(
                                modifier = Modifier,
                                checked = rememberMeChecked,
                                onCheckedChange = { rememberMeChecked = it })

                            Text(text = stringResource(id = R.string.remember_me))
                        }

                        TextButton(
                            modifier = Modifier
                                .padding(end = 50.dp)
                                .align(Alignment.CenterEnd),
                            onClick = {
                                authViewModel.clearCache()
                                onForgotClick()
                            }) {
                            Text(text = stringResource(id = R.string.forgot_password))
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 50.dp)
                ) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier
                            .width(300.dp)
                            .wrapContentHeight()
                            .align(Alignment.TopCenter),
                        containerColor = Blue,
                        onClick = {
                            authViewModel.onEvent(UserEvent.LoginSubmit)
                        }) {
                        Text(
                            text = stringResource(id = R.string.login),
                            color = White
                        )
                    }

                    TextButton(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .align(Alignment.BottomEnd)
                            .padding(60.dp),
                        onClick = {
                            authViewModel.clearCache()
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

@Composable
private fun LoginComposeTextFields(
    valueSet: String,
    onValueChangeSet: (input: String) -> Unit,
    onPlaceHolderId: Int,
    keyboardType: KeyboardType,
    isVisualAllowed: Boolean = false,
    isError: Boolean,
    errorMessage: String,
    onTrailingIconSet: () -> Unit,
    labelId: Int
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = valueSet,
        onValueChange = { onValueChangeSet(it) },
        placeholder = {
            Text(text = stringResource(onPlaceHolderId))
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Start
                )
            }
        },
        visualTransformation = if (!passwordVisibility && isVisualAllowed) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isVisualAllowed) {
                val image =
                    if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description =
                    if (passwordVisibility) stringResource(id = R.string.show_password) else stringResource(
                        id = R.string.hide_password
                    )
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }

            if (valueSet.isNotEmpty() && !isVisualAllowed) {
                IconButton(onClick = {
                    onTrailingIconSet()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.clear)
                    )
                }
            }
        },
        label = { Text(text = stringResource(id = labelId)) }
    )
}




