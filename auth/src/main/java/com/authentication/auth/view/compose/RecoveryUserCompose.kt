package com.authentication.auth.view.compose

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import com.authentication.auth.data.config.AccountRecoveryPreferences
import com.authentication.auth.data.model.Reset
import com.authentication.auth.ui.theme.Black
import com.authentication.auth.ui.theme.Blue
import com.authentication.auth.ui.theme.White
import com.authentication.auth.viewmodel.AuthViewModel
import com.network.state.viewmodel.NetworkStatusViewModel
import com.authentication.auth.viewmodel.UserEvent


@Composable
fun RecoveryUserCompose(
    activity: ComponentActivity,
    networkStatusViewModel: NetworkStatusViewModel,
    authViewModel: AuthViewModel,
    onLoginClick: () -> Unit,
) {
    val snackBarHost = remember { SnackbarHostState() }
    var showSnack by remember { mutableStateOf(false) }
    var serverError by remember { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val userValidationState = authViewModel.personState

    LaunchedEffect(key1 = userValidationState) {
        activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
            authViewModel.validationEvent.collect { event ->
                if (event == AuthViewModel.ValidationEvent.Success) {
                    networkStatusViewModel.start(activity)
                    val accountRecoveryPreferences = AccountRecoveryPreferences(context)
                    authViewModel.reset(
                        Reset(
                            email = authViewModel.getEmailFromSharedPreferences()!!,
                            token = authViewModel.getTokenFromSharedPreferences()!!,
                            newPassword = userValidationState.password,
                            repeatNewPassword = userValidationState.repeatedPassword
                        )
                    ) { res ->
                        if (res.isSuccessful) {
                            accountRecoveryPreferences.clearAll()
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

    if (showDialog) {
        ResetDialog {
            showDialog = false
            authViewModel.clearCache()
            onLoginClick()
        }
    }
    Scaffold { innerPadding ->
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
                                actionLabel = context.getString(
                                    R.string.ok
                                )
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
                            text = stringResource(id = R.string.reset_password),
                            color = Blue,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.please_input_new_password),
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

                        RecoverUserComposeTextFields(
                            valueSet = userValidationState.password,
                            onValueChangeSet = {
                                authViewModel.onEvent(
                                    UserEvent.PasswordChanged(it)
                                )
                            },
                            onPlaceHolderId = R.string.password,
                            isError = userValidationState.passwordError.isNotEmpty(),
                            errorMessage = userValidationState.passwordError,
                            labelId = R.string.password
                        )

                        RecoverUserComposeTextFields(
                            valueSet = userValidationState.repeatedPassword,
                            onValueChangeSet = {
                                authViewModel.onEvent(
                                    UserEvent.RepeatedPasswordChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.repeated_password,
                            isError = userValidationState.repeatedPasswordError.isNotEmpty(),
                            errorMessage = userValidationState.repeatedPasswordError,
                            labelId = R.string.repeated_password
                        )
                    }

                    ExtendedFloatingActionButton(
                        modifier = Modifier
                            .width(300.dp)
                            .wrapContentHeight()
                            .padding(top = 50.dp),
                        containerColor = Blue,
                        onClick = {
                            authViewModel.onEvent(UserEvent.RecoverySubmit)
                        }) {
                        Text(
                            text = stringResource(id = R.string.save),
                            color = White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResetDialog(
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                Icons.Filled.Info,
                contentDescription = stringResource(id = R.string.reset_password)
            )
        },
        title = {
            Text(text = stringResource(id = R.string.reset_password))
        },
        text = {
            Text(
                text = stringResource(id = R.string.yout_password_has_been_reset)
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
private fun RecoverUserComposeTextFields(
    valueSet: String,
    onValueChangeSet: (input: String) -> Unit,
    onPlaceHolderId: Int,
    isError: Boolean,
    errorMessage: String,
    labelId: Int
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = valueSet,
        onValueChange = { onValueChangeSet(it) },
        placeholder = {
            Text(text = stringResource(onPlaceHolderId))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
        visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            val image =
                if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description =
                if (passwordVisibility) stringResource(id = R.string.show_password) else stringResource(
                    id = R.string.hide_password
                )
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        label = { Text(text = stringResource(id = labelId)) }
    )
}