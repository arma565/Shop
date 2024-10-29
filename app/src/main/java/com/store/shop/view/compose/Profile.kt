package com.store.shop.view.compose

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.authentication.auth.data.config.UserAutoLoginConfig
import com.authentication.auth.data.model.User
import com.authentication.auth.ui.theme.Black
import com.authentication.auth.ui.theme.White
import com.authentication.auth.view.activity.AuthViewModelInstance.authenticationViewModel
import com.authentication.auth.viewmodel.AuthenticationViewModel
import com.authentication.auth.viewmodel.UserEvent
import kotlinx.coroutines.launch
import com.store.shop.R

@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCompose(
    onLogout: () -> Unit
) {

    val snackBarHost =
        remember { androidx.compose.material3.SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showSnack by remember {
        mutableStateOf(
            false
        )
    }
    var showDeleteAccountDialog by rememberSaveable {
        mutableStateOf(
            false
        )
    }
    var clearCash by rememberSaveable {
        mutableStateOf(
            false
        )
    }
    val context = LocalContext.current
    val user: User =
        authenticationViewModel.getSpecificUser(UserAutoLoginConfig(context).getUserID())
    if (showDeleteAccountDialog) {
        ProfileDeleteAccountDialog(user,
            authenticationViewModel,
            onConfirmButton = {
                clearCash = true
                onLogout()
            }) {
            showDeleteAccountDialog = false
        }
    }
    if (clearCash) {
        authenticationViewModel.personState =
            com.authentication.auth.viewmodel.UserValidationState()
    }
    val userValidationState = authenticationViewModel.personState
    LaunchedEffect(key1 = userValidationState, key2 = user) {
        authenticationViewModel.validationEvent.collect { event ->
            if (event == AuthenticationViewModel.ValidationEvent.Success) {
                val newUser = user.copy(
                    password = userValidationState.password,
                    repeatedPassword = userValidationState.repeatedPassword
                )
                authenticationViewModel.upsertUser(newUser)
                clearCash = true
                onLogout()
            }
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = 10.dp),
                hostState = snackBarHost
            )
        },
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.edit_profile)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val (constEditProfileCard,
                    constProfileAndChangePasswordDivider,
                    constChangePasswordCard,
                    constChangePasswordAndConsButtonCardDivider,
                    constButtonCard) = createRefs()

                ProfileElevatedCard(
                    modifier = Modifier
                        .constrainAs(constEditProfileCard) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var imageUri by remember { mutableStateOf<Uri?>(null) }
                        val launcherHigherAPI =
                            rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                                if (uri != null) {
                                    imageUri = uri
                                    uri.let {
                                        if (Build.VERSION.SDK_INT < 28) {
                                            authenticationViewModel.onEvent(
                                                UserEvent.ProfilePhotoChanged(
                                                    MediaStore.Images.Media.getBitmap(
                                                        context.contentResolver,
                                                        it
                                                    )
                                                )
                                            )
                                        } else if (Build.VERSION.SDK_INT >= 28) {
                                            authenticationViewModel.onEvent(
                                                UserEvent.ProfilePhotoChanged(
                                                    ImageDecoder.decodeBitmap(
                                                        ImageDecoder.createSource(
                                                            context.contentResolver,
                                                            it
                                                        )
                                                    )
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                        Image(
                            modifier = Modifier
                                .size(80.dp, 80.dp)
                                .clip(shape = CircleShape),
                            bitmap = userValidationState.profilePhoto?.asImageBitmap()
                                ?: ImageBitmap.imageResource(id = R.drawable.user),
                            contentDescription = stringResource(id = R.string.user)
                        )

                        ExtendedFloatingActionButton(
                            modifier = Modifier.padding(top = 20.dp),
                            onClick = {
                                launcherHigherAPI.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }) {
                            Text(text = stringResource(id = R.string.upload_image_profile))
                        }
                        ProfileComposeTextFields(
                            modifier = Modifier
                                .width(320.dp)
                                .wrapContentHeight()
                                .padding(top = 20.dp),
                            valueSet = userValidationState.userName,
                            onValueChangeSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.UserNameChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.username,
                            keyboardType = KeyboardType.Text,
                            onTrailingIconSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.UserNameChanged("")
                                )
                            },
                            labelId = R.string.username
                        )

                        ProfileComposeTextFields(
                            modifier = Modifier
                                .width(320.dp)
                                .wrapContentHeight()
                                .padding(top = 20.dp),
                            valueSet = userValidationState.firstName,
                            onValueChangeSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.FirstNameChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.firstname,
                            keyboardType = KeyboardType.Text,
                            onTrailingIconSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.FirstNameChanged("")
                                )
                            },
                            labelId = R.string.firstname
                        )
                        ProfileComposeTextFields(
                            modifier = Modifier
                                .width(320.dp)
                                .wrapContentHeight()
                                .padding(top = 20.dp),
                            valueSet = userValidationState.lastName,
                            onValueChangeSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.LastNameChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.lastname,
                            keyboardType = KeyboardType.Text,
                            onTrailingIconSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.LastNameChanged("")
                                )
                            },
                            labelId = R.string.lastname
                        )

                        ProfileComposeTextFields(
                            modifier = Modifier
                                .width(320.dp)
                                .wrapContentHeight()
                                .padding(top = 20.dp),
                            valueSet = userValidationState.phoneNumber,
                            onValueChangeSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.PhoneNumberChangedChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.phonenumber,
                            keyboardType = KeyboardType.Text,
                            onTrailingIconSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.PhoneNumberChangedChanged("")
                                )
                            },
                            labelId = R.string.phonenumber
                        )

                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .width(320.dp)
                                .wrapContentHeight(), containerColor = Color.Blue,
                            onClick = {
                                val personStateList = listOf(
                                    userValidationState.userName,
                                    userValidationState.firstName,
                                    userValidationState.lastName,
                                    userValidationState.phoneNumber
                                ).all { it.isEmpty() }
                                if (personStateList) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.please_fill_at_lease_one_field),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@ExtendedFloatingActionButton
                                }
                                coroutineScope.launch {
                                    val newUser = user.copy(
                                        id = user.id,
                                        profilePhoto = userValidationState.profilePhoto,
                                        username = userValidationState.userName,
                                        firstName = userValidationState.firstName,
                                        lastName = userValidationState.lastName,
                                        phoneNumber = userValidationState.phoneNumber
                                    )
                                    authenticationViewModel.upsertUser(newUser)
                                    clearCash = true
                                    onLogout()
                                }
                            }) {
                            Text(text = stringResource(id = R.string.save), color = Color.White)
                        }
                    }
                }

                ProfileHorizontalDivider(modifier = Modifier
                    .constrainAs(constProfileAndChangePasswordDivider) {
                        top.linkTo(constEditProfileCard.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

                ProfileElevatedCard(modifier = Modifier
                    .constrainAs(constChangePasswordCard) {
                        top.linkTo(constProfileAndChangePasswordDivider.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (showSnack && userValidationState.userNotFoundError.isNotEmpty()) {
                            LaunchedEffect(key1 = snackBarHost) {
                                snackBarHost.showSnackbar(
                                    message = context.getString(com.authentication.auth.R.string.user_not_found),
                                    actionLabel = context.getString(com.authentication.auth.R.string.ok)
                                )
                                showSnack = false
                                return@LaunchedEffect
                            }
                        }

                        ProfileComposeTextFields(
                            modifier = Modifier
                                .width(320.dp)
                                .wrapContentHeight()
                                .padding(top = 20.dp),
                            valueSet = userValidationState.currentUserPassword,
                            onValueChangeSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.CurrentUserPasswordChanged(
                                        it
                                    )
                                )
                                authenticationViewModel.onEvent(UserEvent.EmailChanged(user.email))
                            },
                            onPlaceHolderId = R.string.current_password,
                            keyboardType = KeyboardType.Password,
                            isVisualAllowed = true,
                            isError = userValidationState.currentUserPasswordError.isNotEmpty(),
                            errorMessage = userValidationState.currentUserPasswordError,
                            onTrailingIconSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.CurrentUserPasswordChanged("")
                                )
                            },
                            labelId = R.string.current_password
                        )

                        ProfileComposeTextFields(
                            modifier = Modifier
                                .width(320.dp)
                                .wrapContentHeight()
                                .padding(top = 20.dp),
                            valueSet = userValidationState.password,
                            onValueChangeSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.PasswordChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.new_password,
                            keyboardType = KeyboardType.Password,
                            isVisualAllowed = true,
                            isError = userValidationState.passwordError.isNotEmpty(),
                            errorMessage = userValidationState.passwordError,
                            onTrailingIconSet = {
                                authenticationViewModel.onEvent(
                                    UserEvent.PasswordChanged("")
                                )
                            },
                            labelId = R.string.new_password
                        )

                        ProfileComposeTextFields(
                            modifier = Modifier
                                .width(320.dp)
                                .wrapContentHeight()
                                .padding(top = 20.dp),
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

                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .width(320.dp)
                                .wrapContentHeight(), containerColor = Color.Blue,
                            onClick = {
                                showSnack = true
                                authenticationViewModel.onEvent(UserEvent.ChangePasswordSubmit)
                            }) {
                            Text(
                                text = stringResource(id = R.string.change_password),
                                color = Color.White
                            )
                        }
                    }
                }

                ProfileHorizontalDivider(modifier = Modifier
                    .constrainAs(constChangePasswordAndConsButtonCardDivider) {
                        top.linkTo(constChangePasswordCard.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

                ProfileElevatedCard(modifier = Modifier
                    .constrainAs(constButtonCard) {
                        top.linkTo(constChangePasswordAndConsButtonCardDivider.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .width(320.dp)
                                .wrapContentHeight(), containerColor = Color.Red,
                            onClick = {
                                showDeleteAccountDialog = true
                            }) {
                            Text(
                                text = stringResource(id = R.string.delete_account),
                                color = Color.White
                            )
                        }

                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .width(320.dp)
                                .wrapContentHeight(), containerColor = Color.Blue,
                            onClick = {
                                UserAutoLoginConfig(context).clearAll()
                                clearCash = true
                                onLogout()
                            }) {
                            Text(text = stringResource(id = R.string.log_out), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHorizontalDivider(
    modifier: Modifier
) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .border(width = 2.dp, color = Color.Black)
    )
}

@Composable
private fun ProfileElevatedCard(modifier: Modifier, content: @Composable () -> Unit) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp), colors = CardColors(
            containerColor = White,
            disabledContainerColor = Color.Gray,
            contentColor = Black,
            disabledContentColor = Color.Gray
        ), elevation = CardDefaults.cardElevation(10.dp)
    ) {
        content()
    }
}

@Composable
private fun ProfileComposeTextFields(
    modifier: Modifier,
    valueSet: String,
    onValueChangeSet: (input: String) -> Unit,
    onPlaceHolderId: Int,
    keyboardType: KeyboardType,
    isVisualAllowed: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "",
    onTrailingIconSet: () -> Unit,
    labelId: Int
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
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
                    if (passwordVisibility) stringResource(id = com.authentication.auth.R.string.show_password) else stringResource(
                        id = com.authentication.auth.R.string.hide_password
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
                        contentDescription = stringResource(id = com.authentication.auth.R.string.clear)
                    )
                }
            }
        },
        label = { Text(text = stringResource(id = labelId)) }
    )
}


@Composable
private fun ProfileDeleteAccountDialog(
    user: User,
    authenticationViewModel: AuthenticationViewModel,
    onConfirmButton: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(icon = {
        Icon(
            Icons.Filled.Info,
            contentDescription = stringResource(id = R.string.delete)
        )
    },
        title = {
            Text(text = stringResource(id = R.string.delete))
        },
        text = { Text(text = stringResource(id = R.string.are_you_sure)) },
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                authenticationViewModel.deleteUser(user)
                onConfirmButton()
            }) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismissClick()
            }) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        })
}
