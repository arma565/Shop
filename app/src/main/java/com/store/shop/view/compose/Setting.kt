package com.store.shop.view.compose

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.authentication.auth.viewmodel.AuthViewModel
import com.store.shop.R
import com.store.shop.view.activity.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingCompose(
    authViewModel: AuthViewModel,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    onAccountClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    var userFirstName: String? by remember { mutableStateOf("") }
    var userName: String? by remember { mutableStateOf("") }
    var darkModeTextChangeState: String? by remember { mutableStateOf(null) }
    var userProfileImage: Bitmap? by remember { mutableStateOf(null) }
    var onShowAboutDialog: Boolean by remember { mutableStateOf(false) }

    if (onShowAboutDialog) {
        AboutAppDialog {
            onShowAboutDialog = false
        }
    }

    darkModeTextChangeState = if (darkTheme)
        stringResource(id = R.string.on)
    else
        stringResource(id = R.string.off)


    val user = authViewModel.user.collectAsState().value
    val userProfilePhoto = authViewModel.profilePhoto.collectAsState().value
    LaunchedEffect(true) {
        authViewModel.getUser(
            authViewModel.getUserNameFromSharedPreferences() ?: ""
        )
    }

    LaunchedEffect(key1 = user) {
        authViewModel.downloadProfileImage(user.username)
    }
    userName = user.username.ifEmpty { stringResource(id = R.string.no_user_name) }
    userFirstName = user.firstName.ifEmpty { stringResource(id = R.string.no_name) }
    userProfileImage = userProfilePhoto

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.setting),
                    fontWeight = FontWeight.Bold
                )
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val (txtAccountConst, accountRowConst, txtGeneralConst, darkModeRowConst,
                    txtHistoryConst, historyRowConst, txtNotificationsConst, notificationsRowConst, txtAboutAppConst, aboutAppRowConst) = createRefs()

                Text(
                    modifier = Modifier
                        .constrainAs(txtAccountConst) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .padding(innerPadding),
                    text = stringResource(id = R.string.account),
                    fontSize = 20.sp
                )

                Box(
                    modifier = Modifier
                        .constrainAs(accountRowConst) {
                            top.linkTo(txtAccountConst.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            onAccountClick()
                        }
                        .padding(20.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(50.dp, 50.dp)
                            .align(Alignment.CenterStart),
                        bitmap = userProfileImage?.asImageBitmap()
                            ?: ImageBitmap.imageResource(id = R.drawable.user),
                        contentDescription = stringResource(id = R.string.user)
                    )
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .align(Alignment.CenterStart)
                            .padding(start = 60.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            text = userFirstName!!,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            text = userName!!,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = stringResource(id = R.string.account)
                    )
                }

                Text(
                    modifier = Modifier
                        .constrainAs(txtGeneralConst) {
                            top.linkTo(accountRowConst.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 20.dp),
                    text = stringResource(id = R.string.general),
                    fontSize = 20.sp
                )

                Box(
                    modifier = Modifier
                        .constrainAs(darkModeRowConst) {
                            top.linkTo(txtGeneralConst.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(20.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(50.dp, 50.dp)
                            .align(Alignment.CenterStart),
                        painter = painterResource(id = R.drawable.moon),
                        contentDescription = stringResource(id = R.string.dark_mode)
                    )
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .align(Alignment.CenterStart)
                            .padding(start = 60.dp),
                        text = stringResource(id = R.string.dark_mode),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            text = darkModeTextChangeState ?: stringResource(id = R.string.off),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        ThemeSwitcher(
                            darkTheme = darkTheme,
                            size = 50.dp,
                            onClick = {
                                onThemeUpdated()
                            }
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .constrainAs(txtHistoryConst) {
                            top.linkTo(darkModeRowConst.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 20.dp),
                    text = stringResource(id = R.string.history),
                    fontSize = 20.sp
                )
                Box(
                    modifier = Modifier
                        .constrainAs(historyRowConst) {
                            top.linkTo(txtHistoryConst.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(20.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(50.dp, 50.dp)
                            .align(Alignment.CenterStart),
                        painter = painterResource(id = R.drawable.history),
                        contentDescription = stringResource(id = R.string.history)
                    )
                }

                Text(
                    modifier = Modifier
                        .constrainAs(txtNotificationsConst) {
                            top.linkTo(historyRowConst.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 20.dp),
                    text = stringResource(id = R.string.notifications),
                    fontSize = 20.sp
                )
                Box(
                    modifier = Modifier
                        .constrainAs(notificationsRowConst) {
                            top.linkTo(txtNotificationsConst.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            onNotificationClick()
                        }
                        .padding(20.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(50.dp, 50.dp)
                            .align(Alignment.CenterStart),
                        painter = painterResource(id = R.drawable.notif),
                        contentDescription = stringResource(id = R.string.notifications)
                    )
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .align(Alignment.CenterStart)
                            .padding(start = 60.dp),
                        text = stringResource(id = R.string.notifications),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = stringResource(id = R.string.notifications)
                    )
                }

                Text(
                    modifier = Modifier
                        .constrainAs(txtAboutAppConst) {
                            top.linkTo(notificationsRowConst.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(top = 20.dp),
                    text = stringResource(id = R.string.about_title),
                    fontSize = 20.sp
                )
                Box(
                    modifier = Modifier
                        .constrainAs(aboutAppRowConst) {
                            top.linkTo(txtAboutAppConst.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            onShowAboutDialog = true
                        }
                        .padding(20.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(50.dp, 50.dp)
                            .align(Alignment.CenterStart),
                        painter = painterResource(id = R.drawable.help),
                        contentDescription = stringResource(id = R.string.about_app)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.CenterStart)
                            .padding(start = 60.dp),
                        text = stringResource(id = R.string.about_app),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = stringResource(id = R.string.about_app)
                    )
                }
            }
        }
    }
}


@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun ThemeSwitcher(
    darkTheme: Boolean = false,
    size: Dp = 150.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {
    val offset by animateDpAsState(
        targetValue = if (darkTheme) 0.dp else size,
        animationSpec = animationSpec,
        label = "Animation"
    )

    Box(
        modifier = Modifier
            .width(size * 2)
            .height(size)
            .clip(shape = parentShape)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
                .offset(x = offset)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.Nightlight,
                    contentDescription = "Theme Icon",
                    tint = if (darkTheme) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "Theme Icon",
                    tint = if (darkTheme) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@Composable
private fun AboutAppDialog(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(5.dp),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .border(2.dp, color = Orange, shape = RoundedCornerShape(15.dp))

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.shop),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(id = R.string.app_version) + "1.0",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Thin
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = {
                        onDismissRequest()
                    }) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                }
            }
        }
    }
}

