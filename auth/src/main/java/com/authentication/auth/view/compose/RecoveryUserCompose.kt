package com.authentication.auth.view.compose

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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.authentication.auth.view.activity.AuthViewModelInstance.authenticationViewModel
import com.authentication.auth.viewmodel.AuthenticationViewModel
import com.authentication.auth.viewmodel.UserEvent

@Composable
fun RecoveryUserCompose(
    user: User,
    onLoginClick: () -> Unit,
) {
    val userValidationState = authenticationViewModel.personState
    LaunchedEffect(key1 = userValidationState) {
        authenticationViewModel.validationEvent.collect { event ->
            if (event == AuthenticationViewModel.ValidationEvent.Success) {
                authenticationViewModel.upsertUser(User(
                    id = user.id,
                    username = user.username,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    phoneNumber = user.phoneNumber,
                    email = user.email,
                    password = userValidationState.password,
                    recoveryCode = user.recoveryCode,
                    repeatedPassword = userValidationState.repeatedPassword,
                    profilePhoto = user.profilePhoto
                ))
                onLoginClick()
            }
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
                                authenticationViewModel.onEvent(
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
                                authenticationViewModel.onEvent(
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
                            authenticationViewModel.onEvent(UserEvent.RecoverySubmit)
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