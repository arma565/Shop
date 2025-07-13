package com.store.shop.view.compose

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.authentication.auth.data.config.UserAutoLoginPreferencesRepository
import com.authentication.auth.data.model.Change
import com.authentication.auth.data.model.Login
import com.authentication.auth.data.model.Profile
import com.authentication.auth.data.use_case.ValidateUserPassword
import com.authentication.auth.data.use_case.ValidateUserRepeatedPassword
import com.authentication.auth.ui.theme.Black
import com.authentication.auth.ui.theme.White
import com.authentication.auth.viewmodel.AuthViewModel
import com.authentication.auth.viewmodel.UserEvent
import com.network.state.viewmodel.NetworkStatusViewModel
import com.store.shop.R
import java.io.File

@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCompose(
    authViewModelMainActivity: AuthViewModel,
    activity: ComponentActivity,
    networkStatusViewModel: NetworkStatusViewModel,
    onLogout: () -> Unit
) {
    val snackBarHost = remember { SnackbarHostState() }
    var showSnack by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by rememberSaveable { mutableStateOf(false) }
    var showChangeAccountPasswordDialog by rememberSaveable { mutableStateOf(false) }
    var profilePhoto: Bitmap? by remember { mutableStateOf(null) }
    var serverError by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (showDeleteAccountDialog) {
        ProfileDeleteAccountDialog(
            authViewModelMainActivity = authViewModelMainActivity,
            context = context,
            onConfirmClick = {
                UserAutoLoginPreferencesRepository(context).clearAll()
                onLogout()
            }, onFailureClick = {
                serverError = it
                showSnack = true
                showDeleteAccountDialog = false
            }) {
            showDeleteAccountDialog = false
        }
    }

    if (showChangeAccountPasswordDialog) {
        ChangeAccountPasswordDialog(
            authViewModelMainActivity = authViewModelMainActivity,
            context = context,
            onServerErrorInvoked = {
                serverError = it
                showSnack = true
                showChangeAccountPasswordDialog = false
            }, onConfirmClick = {
                serverError = it
                showSnack = true
                onLogout()
            }) {
            showChangeAccountPasswordDialog = false
        }
    }

    val userValidationState = authViewModelMainActivity.personState
    LaunchedEffect(key1 = userValidationState) {
        activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
            authViewModelMainActivity.validationEvent.collect { event ->
                if (event == AuthViewModel.ValidationEvent.Success) {
                    networkStatusViewModel.start(activity)

                    if (profilePhoto != null) {
                        profilePhoto?.let { userProfileImage ->
                            authViewModelMainActivity.saveBitmapToCacheDir(
                                context = context,
                                userProfileImage
                            ) { savedBitmap ->
                                if (savedBitmap != null) {
                                    uploadProfilePhoto(
                                        authViewModelMainActivity = authViewModelMainActivity,
                                        savedBitmap,
                                        userValidationState.userName,
                                        onSuccessful = {
                                            editUserProfile(
                                                Profile(
                                                    userName = userValidationState.userName,
                                                    firstName = userValidationState.firstName,
                                                    lastName = userValidationState.lastName,
                                                    phoneNumber = userValidationState.phoneNumber
                                                ),
                                                onSuccessful = {
                                                    onLogout()
                                                },
                                                authViewModelMainActivity = authViewModelMainActivity
                                            ) { errorMessage ->
                                                serverError = errorMessage
                                                    ?: context.getString(R.string.unknown_error)
                                                showSnack = true
                                            }
                                        }) {
                                        serverError =
                                            context.getString(R.string.image_uploaded_failed)
                                        showSnack = true
                                    }
                                } else {
                                    serverError =
                                        context.getString(R.string.could_n_t_save_file_to_cache)
                                    showSnack = true
                                }
                            }
                        }
                    }
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

                        var imageUri by remember { mutableStateOf<Uri?>(null) }
                        val launcherHigherAPI =
                            rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                                imageUri = uri
                                if (imageUri == null)
                                    return@rememberLauncherForActivityResult
                                profilePhoto = if (Build.VERSION.SDK_INT < 28) {
                                    MediaStore.Images.Media.getBitmap(
                                        context.contentResolver,
                                        imageUri
                                    )
                                } else {
                                    ImageDecoder.decodeBitmap(
                                        ImageDecoder.createSource(
                                            context.contentResolver,
                                            imageUri!!
                                        )
                                    )
                                }
                            }

                        Image(
                            modifier = Modifier
                                .size(80.dp, 80.dp)
                                .clip(shape = CircleShape),
                            bitmap = profilePhoto?.asImageBitmap()
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
                                authViewModelMainActivity.onEvent(
                                    UserEvent.UserNameChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.username,
                            keyboardType = KeyboardType.Text,
                            isError = userValidationState.userNameError.isNotEmpty(),
                            errorMessage = userValidationState.userNameError,
                            onTrailingIconSet = {
                                authViewModelMainActivity.onEvent(
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
                                authViewModelMainActivity.onEvent(
                                    UserEvent.FirstNameChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.firstname,
                            keyboardType = KeyboardType.Text,
                            isError = userValidationState.firstNameError.isNotEmpty(),
                            errorMessage = userValidationState.firstNameError,
                            onTrailingIconSet = {
                                authViewModelMainActivity.onEvent(
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
                                authViewModelMainActivity.onEvent(
                                    UserEvent.LastNameChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.lastname,
                            keyboardType = KeyboardType.Text,
                            isError = userValidationState.lastNameError.isNotEmpty(),
                            errorMessage = userValidationState.lastNameError,
                            onTrailingIconSet = {
                                authViewModelMainActivity.onEvent(
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
                                authViewModelMainActivity.onEvent(
                                    UserEvent.PhoneNumberChangedChanged(
                                        it
                                    )
                                )
                            },
                            onPlaceHolderId = R.string.phone_number,
                            keyboardType = KeyboardType.Text,
                            isError = userValidationState.phoneNumberError.isNotEmpty(),
                            errorMessage = userValidationState.phoneNumberError,
                            onTrailingIconSet = {
                                authViewModelMainActivity.onEvent(
                                    UserEvent.PhoneNumberChangedChanged("")
                                )
                            },
                            labelId = R.string.phone_number
                        )

                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .width(320.dp)
                                .wrapContentHeight(), containerColor = Color.Blue,
                            onClick = {
                                authViewModelMainActivity.onEvent(UserEvent.UpdateProfileSubmit)
                            }) {
                            Text(text = stringResource(id = R.string.save), color = Color.White)
                        }
                    }
                }

                ProfileHorizontalDivider(
                    modifier = Modifier
                        .constrainAs(constChangePasswordAndConsButtonCardDivider) {
                            top.linkTo(constEditProfileCard.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })

                ProfileElevatedCard(
                    modifier = Modifier
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
                                .wrapContentHeight(), containerColor = Color.Blue,
                            onClick = {
                                showChangeAccountPasswordDialog = true
//                                authViewModelMainActivity.onEvent(UserEvent.ChangePasswordSubmit)
                            }) {
                            Text(
                                text = stringResource(id = R.string.change_password),
                                color = Color.White
                            )
                        }

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
                                UserAutoLoginPreferencesRepository(context).clearAll()
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

@Composable
private fun ProfileDeleteAccountDialog(
    context: Context,
    authViewModelMainActivity: AuthViewModel,
    onConfirmClick: () -> Unit,
    onFailureClick: (serverError: String) -> Unit,
    onDismissClick: () -> Unit
) {
    val userName = authViewModelMainActivity.getUserNameFromSharedPreferences() ?: ""
    var currentPassword by remember { mutableStateOf("") }
    var currentPasswordError by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismissClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    ProfileComposeTextFields(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 10.dp),
                        valueSet = currentPassword,
                        onValueChangeSet = {
                            currentPassword = it
                        },
                        onPlaceHolderId = R.string.password,
                        keyboardType = KeyboardType.Password,
                        isVisualAllowed = true,
                        isError = currentPasswordError.isNotEmpty(),
                        errorMessage = currentPasswordError,
                        onTrailingIconSet = {
                            currentPassword = ""
                        },
                        labelId = R.string.password
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissClick() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.dismiss))
                    }
                    TextButton(
                        onClick = {
                            val login = Login(
                                userName = userName,
                                password = currentPassword
                            )
                            val currentPasswordResult =
                                ValidateUserPassword(context).execute(login.password)

                            val hasError = listOf(
                                currentPasswordResult,
                            ).any { !it.successful }

                            if (hasError) {
                                currentPasswordError = currentPasswordResult.errorMessage
                                return@TextButton
                            } else {

                                authViewModelMainActivity.deleteUser(login) { res ->
                                    if (res.isSuccessful) {
                                        onConfirmClick()
                                    } else {
                                        onFailureClick(
                                            res.errorBody()?.string()
                                                ?: context.getString(R.string.unknown_error)
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

@Composable
private fun ChangeAccountPasswordDialog(
    context: Context,
    authViewModelMainActivity: AuthViewModel,
    onServerErrorInvoked: (serverError: String) -> Unit,
    onConfirmClick: (successfulMessage: String) -> Unit,
    onDismissClick: () -> Unit
) {
    val userName = authViewModelMainActivity.getUserNameFromSharedPreferences() ?: ""
    var currentPassword by remember { mutableStateOf("") }
    var currentPasswordError by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf("") }
    var repeatNewPassword by remember { mutableStateOf("") }
    var repeatNewPasswordError by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismissClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    ProfileComposeTextFields(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 10.dp),
                        valueSet = currentPassword,
                        onValueChangeSet = {
                            currentPassword = it
                        },
                        onPlaceHolderId = R.string.current_password,
                        keyboardType = KeyboardType.Password,
                        isVisualAllowed = true,
                        isError = currentPasswordError.isNotEmpty(),
                        errorMessage = currentPasswordError,
                        onTrailingIconSet = {
                            currentPassword = ""
                        },
                        labelId = R.string.current_password
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    ProfileComposeTextFields(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 10.dp),
                        valueSet = newPassword,
                        onValueChangeSet = {
                            newPassword = it
                        },
                        onPlaceHolderId = R.string.new_password,
                        keyboardType = KeyboardType.Password,
                        isVisualAllowed = true,
                        isError = newPasswordError.isNotEmpty(),
                        errorMessage = newPasswordError,
                        onTrailingIconSet = {
                            newPassword = ""
                        },
                        labelId = R.string.new_password
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    ProfileComposeTextFields(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 10.dp),
                        valueSet = repeatNewPassword,
                        onValueChangeSet = {
                            repeatNewPassword = it
                        },
                        onPlaceHolderId = R.string.repeated_password,
                        keyboardType = KeyboardType.Password,
                        isVisualAllowed = true,
                        isError = repeatNewPasswordError.isNotEmpty(),
                        errorMessage = repeatNewPasswordError,
                        onTrailingIconSet = {
                            repeatNewPassword = ""
                        },
                        labelId = R.string.repeated_password
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissClick() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            val change = Change(
                                userName = userName,
                                currentPassword = currentPassword,
                                newPassword = newPassword,
                                repeatNewPassword = repeatNewPassword
                            )
                            val currentPasswordResult =
                                ValidateUserPassword(context).execute(change.currentPassword)
                            val newPasswordResult =
                                ValidateUserPassword(context).execute(change.newPassword)
                            val newRepeatedPasswordResult =
                                ValidateUserRepeatedPassword(context).execute(
                                    change.newPassword,
                                    change.repeatNewPassword
                                )
                            val hasError = listOf(
                                currentPasswordResult,
                                newPasswordResult,
                                newRepeatedPasswordResult
                            ).any { !it.successful }

                            if (hasError) {
                                currentPasswordError = currentPasswordResult.errorMessage
                                newPasswordError = newPasswordResult.errorMessage
                                repeatNewPasswordError = newRepeatedPasswordResult.errorMessage
                                return@TextButton
                            } else {
                                authViewModelMainActivity.change(change) { res ->
                                    if (res.isSuccessful) {
                                        onConfirmClick(res.body()?.string()!!)
                                    } else {
                                        onServerErrorInvoked(res.errorBody()?.string()!!)
                                    }
                                }
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}


fun uploadProfilePhoto(
    authViewModelMainActivity: AuthViewModel,
    profilePhoto: File,
    userName: String,
    onSuccessful: () -> Unit,
    onFailure: () -> Unit
) {
    authViewModelMainActivity.uploadProfileImage(
        userName,
        profilePhoto
    ) { res ->
        if (res.isSuccessful) {
            onSuccessful()
        } else {
            onFailure()
        }
    }
}

fun editUserProfile(
    profile: Profile,
    authViewModelMainActivity: AuthViewModel,
    onSuccessful: () -> Unit,
    onFailure: (errorMessage: String?) -> Unit
) {
    authViewModelMainActivity.editProfile(
        profile
    ) { res ->
        if (res.isSuccessful) {
            onSuccessful()
        } else {
            onFailure(res.errorBody()?.string())
        }
    }
}