package com.authentication.auth.view.compose

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
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
import com.authentication.auth.R
import com.authentication.auth.data.model.User
import com.authentication.auth.ui.theme.Black
import com.authentication.auth.ui.theme.Blue
import com.authentication.auth.ui.theme.White
import com.authentication.auth.view.activity.AuthViewModelInstance.authNetworkViewModel
import com.authentication.auth.view.activity.AuthViewModelInstance.authenticationViewModel
import com.authentication.auth.viewmodel.AuthenticationViewModel
import com.authentication.auth.viewmodel.UserEvent

@Composable
fun RegisterCompose(
    activity: ComponentActivity,
    onLoginClick: () -> Unit
) {
    val snackBarHost = remember { SnackbarHostState() }
    var showProgressBar by remember { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showSnack by remember { mutableStateOf(false) }
    val userValidationState = authenticationViewModel.personState
    when (true) {
        showDialog -> {
            RegisterDialog {
                showDialog = false
                onLoginClick()
            }
        }

        showProgressBar -> {
            CircularProgressIndicator()
        }

        else -> {}
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = userValidationState) {
        authenticationViewModel.validationEvent.collect { event ->
            showProgressBar = true
            if (event == AuthenticationViewModel.ValidationEvent.Success) {
                try {
                    authNetworkViewModel.register(
                        userValidationState.email,
                        userValidationState.password
                    ).observe(activity) { registerResult ->
                        if (registerResult == 0.0) throw Error()
                        showProgressBar = false
                        authenticationViewModel.upsertUser(
                            User(
                                email = userValidationState.email,
                                password = userValidationState.password,
                                repeatedPassword = userValidationState.repeatedPassword,
                                recoveryCode = (1548..4781345).random().toString(),
                                acceptTerms = userValidationState.acceptedTerms
                            )
                        )
                        showDialog = true
                    }
                } catch (e: Error) {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.un_success),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = 10.dp),
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
                    if (showSnack && userValidationState.userAlreadyExistError.isNotEmpty()) {
                        LaunchedEffect(key1 = snackBarHost) {
                            snackBarHost.showSnackbar(
                                message = userValidationState.userAlreadyExistError,
                                actionLabel = context.getString(R.string.ok)
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
                    )
                    {
                        Text(
                            text = stringResource(id = R.string.register),
                            color = Blue,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.create_your_account),
                            modifier = Modifier.padding(top = 16.dp),
                            color = Blue
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        RegisterComposeTextFields(
                            valueSet = userValidationState.email,
                            onValueChangeSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.EmailChanged(it)
                                )
                            },
                            onPlaceHolderId = R.string.email_address,
                            keyboardType = KeyboardType.Email,
                            isError = userValidationState.emailError.isNotEmpty(),
                            errorMessage = userValidationState.emailError,
                            onTrailingIconSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.EmailChanged(
                                        ""
                                    )
                                )
                            },
                            labelId = R.string.email
                        )

                        RegisterComposeTextFields(
                            valueSet = userValidationState.password,
                            onValueChangeSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.PasswordChanged(it)
                                )
                            },
                            onPlaceHolderId = R.string.password,
                            keyboardType = KeyboardType.Password,
                            isVisualAllowed = true,
                            isError = userValidationState.passwordError.isNotEmpty(),
                            errorMessage = userValidationState.passwordError,
                            onTrailingIconSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.PasswordChanged("")
                                )
                            },
                            labelId = R.string.password
                        )

                        RegisterComposeTextFields(
                            valueSet = userValidationState.repeatedPassword,
                            onValueChangeSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.RepeatedPasswordChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.repeated_password,
                            keyboardType = KeyboardType.Password,
                            isVisualAllowed = true,
                            isError = userValidationState.repeatedPasswordError.isNotEmpty(),
                            errorMessage = userValidationState.repeatedPasswordError,
                            onTrailingIconSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.RepeatedPasswordChanged("")
                                )
                            },
                            labelId = R.string.repeated_password
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 20.dp, start = 70.dp),
                                text = stringResource(id = R.string.by_registering_you_are_agreeing_to_our_n_terms_of_use_and_privacy_policy),
                                maxLines = 3,
                                color = Blue
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(start = 70.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(text = stringResource(id = R.string.accept_terms))
                                Checkbox(
                                    checked = userValidationState.acceptedTerms,
                                    onCheckedChange = {
                                        authenticationViewModel.onEvent(
                                            UserEvent.AcceptTermsChanged(
                                                it
                                            )
                                        )
                                    })
                            }
                            if (userValidationState.acceptTermsError.isNotEmpty()) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(start = 70.dp),
                                    text = userValidationState.acceptTermsError,
                                    color = MaterialTheme.colorScheme.error,
                                )
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
                                showSnack = true
                                authenticationViewModel.onEvent(UserEvent.RegisterSubmit)
                            }) {
                            Text(
                                text = stringResource(id = R.string.sign_up),
                                color = White
                            )
                        }

                        TextButton(modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(60.dp),
                            onClick = {
                                onLoginClick()
                            }) {
                            Text(
                                text = "${stringResource(id = R.string.already_have_an_account)} ${
                                    stringResource(
                                        id = R.string.login
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

@Composable
private fun RegisterDialog(
    onDismissRequest: () -> Unit
) {
    val list: List<User> = authenticationViewModel.getUserList.collectAsState().value
    if (list.isEmpty()) return
    val userRecoveryCode: String = list.first().recoveryCode
    AlertDialog(icon = {
        Icon(
            Icons.Filled.Info,
            contentDescription = stringResource(id = R.string.register)
        )
    },
        title = {
            Text(text = stringResource(id = R.string.register))
        },
        text = {
            Text(
                text = stringResource(id = R.string.your_registration_recovery_code_is).plus(
                    userRecoveryCode
                )
                    .plus("\n").plus(
                        stringResource(id = R.string.please_keep_it_for_recovery_assistance)
                    )
            )
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                onDismissRequest()
            }) {
                Text(text = stringResource(R.string.confirm))
            }
        })
}


@Composable
private fun RegisterComposeTextFields(
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