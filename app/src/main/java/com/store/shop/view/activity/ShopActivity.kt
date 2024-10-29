package com.store.shop.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.authentication.auth.data.model.GlobalFunctions
import com.authentication.auth.data.model.GlobalFunctions.FadeInAnimation
import com.authentication.auth.view.activity.AuthViewModelInstance
import com.authentication.auth.viewmodel.AuthenticationViewModel
import com.store.shop.R
import com.store.shop.view.activity.ShopActivity.Constance.CATEGORY
import com.store.shop.view.activity.ShopActivity.Constance.CATEGORY_LIST
import com.store.shop.view.activity.ShopActivity.Constance.FAVORITE
import com.store.shop.view.activity.ShopActivity.Constance.HOME
import com.store.shop.view.activity.ShopActivity.Constance.PRODUCT
import com.store.shop.view.activity.ShopActivity.Constance.PROFILE
import com.store.shop.view.activity.ShopActivity.Constance.SETTING
import com.store.shop.view.activity.ui.theme.ShopTheme
import com.store.shop.view.compose.CategoryCompose
import com.store.shop.view.compose.FavoriteCompose
import com.store.shop.view.compose.HomeCompose
import com.store.shop.view.compose.ProductCompose
import com.store.shop.view.compose.ProductListCompose
import com.store.shop.view.compose.ProfileCompose
import com.store.shop.view.compose.SettingCompose
import com.store.shop.viewmodel.LocalShopViewModel
import com.store.shop.viewmodel.RemoteShopViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopActivity : ComponentActivity() {
    private val localShopViewModel: LocalShopViewModel by viewModels()
    private val remoteShopViewModel: RemoteShopViewModel by viewModels()
    private val authenticationViewModel : AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Shop()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Shop() {
        AuthViewModelInstance.authenticationViewModel = authenticationViewModel
        val navController = rememberNavController()
        var darkTheme by remember { mutableStateOf(false) }
        var openSettingInvoke by remember { mutableStateOf(false) }
        ShopTheme {
            val items = listOf(
                BottomNavigationItems(
                    title = stringResource(id = R.string.home),
                    selectedItem = Icons.Filled.Home,
                    unSelectedItem = Icons.Outlined.Home
                ),
                BottomNavigationItems(
                    title = stringResource(id = R.string.category),
                    selectedItem = Icons.Filled.Category,
                    unSelectedItem = Icons.Outlined.Category
                ),
                BottomNavigationItems(
                    title = stringResource(id = R.string.favorite),
                    selectedItem = Icons.Filled.Favorite,
                    unSelectedItem = Icons.Outlined.FavoriteBorder
                ),
            )
            var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(
                            text = stringResource(id = R.string.shop),
                            fontWeight = FontWeight.Bold
                        )
                    }, actions = {
                        IconButton(onClick = {
                            openSettingInvoke = true
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(
                                    id = R.string.setting
                                )
                            )
                        }
                    })
                },
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        items.forEachIndexed { index, bottomNavigationItem ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
                                    when (index) {
                                        NavigationItems.HOME.ordinal -> {
                                            navController.navigate(HOME)
                                        }

                                        NavigationItems.CATEGORY.ordinal -> {
                                            navController.navigate(CATEGORY)
                                        }

                                        NavigationItems.FAVORITE.ordinal -> {
                                            navController.navigate(FAVORITE)
                                        }
                                    }
                                },
                                label = { Text(text = bottomNavigationItem.title) },
                                alwaysShowLabel = false,
                                icon = {
                                    Icon(
                                        imageVector = if (selectedItemIndex == index) bottomNavigationItem.selectedItem!! else bottomNavigationItem.unSelectedItem!!,
                                        contentDescription = bottomNavigationItem.title
                                    )
                                })
                        }
                    }
                }
            ) { innerPadding ->
                if (openSettingInvoke) {
                    navController.navigate(SETTING)
                    openSettingInvoke = false
                }
                NavHost(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                    navController = navController,
                    startDestination = HOME
                ) {

                    composable(HOME) {
                        FadeInAnimation {
                            HomeCompose(remoteShopViewModel) {
                                navController.navigate(PRODUCT)
                            }
                        }
                    }

                    composable(PRODUCT) {
                        FadeInAnimation {
                            ProductCompose(remoteShopViewModel, localShopViewModel)
                        }
                    }

                    composable(CATEGORY) {
                        FadeInAnimation {
                            CategoryCompose(remoteShopViewModel, onCategoryItemClick = {
                                navController.navigate("$CATEGORY_LIST/$it")
                            })
                        }
                    }

                    composable("$CATEGORY_LIST/{id}", arguments = listOf(navArgument("id") {
                        type = NavType.StringType
                    })) { navBackStackEntry ->
                        navBackStackEntry.arguments?.getString("id").let { categoryId ->
                            FadeInAnimation {
                                ProductListCompose(
                                    remoteShopViewModel,
                                    categoryId!!,
                                    onProductClick = {
                                        navController.navigate(PRODUCT)
                                    })
                            }
                        }
                    }

                    composable(FAVORITE) {
                        FadeInAnimation {
                            FavoriteCompose(
                                remoteShopViewModel,
                                localShopViewModel,
                                onProductClick = {
                                    navController.navigate(PRODUCT)
                                })
                        }
                    }

                    composable(SETTING) {
                        FadeInAnimation {
                            SettingCompose(
                                context = applicationContext,
                                darkTheme = darkTheme,
                                onThemeUpdated = {
                                    darkTheme = !darkTheme
                                },
                                onAccountClick = {
                                    navController.navigate(PROFILE)
                                },
                                onClearHistoryClick = {
                                    selectedItemIndex = 0
                                    navController.navigate(HOME)
                                },
                                onNotificationClick = {
                                    openNotificationSettings(this@ShopActivity)
                                })
                        }
                    }
                    composable(PROFILE) {
                        FadeInAnimation {
                            ProfileCompose(
                                onLogout = {
                                    GlobalFunctions.logOut(this@ShopActivity)
                                })
                        }
                    }
                }
            }
        }
    }

    data class BottomNavigationItems(
        val title: String = "",
        val selectedItem: ImageVector? = null,
        val unSelectedItem: ImageVector? = null,
    )

    enum class NavigationItems {
        HOME,
        CATEGORY,
        FAVORITE
    }

    object Constance {
        const val HOME = "home"
        const val PRODUCT = "product"
        const val CATEGORY = "category"
        const val FAVORITE = "favorite"
        const val CATEGORY_LIST = "category_list"
        const val SETTING = "setting"
        const val PROFILE = "profile"
    }

    private fun openNotificationSettings(activity: ComponentActivity) {
        val intent = Intent()
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("app_package", activity.packageName)
        intent.putExtra("app_uid", activity.applicationInfo.uid)
        intent.putExtra("android.provider.extra.APP_PACKAGE", activity.packageName)
        activity.startActivity(intent)
    }
}