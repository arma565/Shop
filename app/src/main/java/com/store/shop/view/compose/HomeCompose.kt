package com.store.shop.view.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.store.shop.R
import com.store.shop.data.model.BaseCategory
import com.store.shop.data.model.BaseHome
import com.store.shop.data.model.New
import com.store.shop.data.model.Product
import com.store.shop.view.compose.DataConstant.BASE_CATEGORY
import com.store.shop.view.compose.DataConstant.BASE_HOME
import com.store.shop.viewmodel.RemoteShopViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeCompose(remoteShopViewModel: RemoteShopViewModel, onProductClick: () -> Unit) {

    val newsList: List<New> = loadNews(remoteShopViewModel).value.getOrDefault(emptyList())

    BASE_HOME = remoteShopViewModel.getBaseHome.value!!
    BASE_CATEGORY = remoteShopViewModel.getBaseCategory.value!!

    val pagerState = rememberPagerState(pageCount = {
        newsList.size
    })

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) { innerPadding ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(15.dp)
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(), state = pagerState
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                            painter = rememberAsyncImagePainter(newsList[page].icon),
                            contentDescription = newsList[page].title,
                        )
                        Row(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(pagerState.pageCount) { iteration ->
                                val color =
                                    if (pagerState.currentPage == iteration) Black else White
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            ShopColumn(
                remoteShopViewModel,
                title = stringResource(id = R.string.amazing_offer),
                BASE_HOME.amazingOffer,
                onProductClick
            )
            ShopColumn(
                remoteShopViewModel,
                title = stringResource(id = R.string.discount),
                BASE_HOME.discount,
                onProductClick
            )
            ShopColumn(
                remoteShopViewModel,
                title = stringResource(id = R.string.mobiles),
                BASE_HOME.mobile,
                onProductClick
            )
            ShopColumn(
                remoteShopViewModel,
                title = stringResource(id = R.string.makeup),
                BASE_HOME.makeup,
                onProductClick
            )
            ShopColumn(
                remoteShopViewModel,
                title = stringResource(id = R.string.mode),
                BASE_CATEGORY.mode,
                onProductClick
            )
            ShopColumn(
                remoteShopViewModel,
                title = stringResource(id = R.string.sport),
                BASE_CATEGORY.sport,
                onProductClick
            )
            ShopColumn(
                remoteShopViewModel,
                title = stringResource(id = R.string.home),
                BASE_CATEGORY.home,
                onProductClick
            )
        }
    }
}

@Composable
fun loadNews(remoteShopViewModel: RemoteShopViewModel): State<Result<List<New>>> {
    return produceState(initialValue = Result.success(emptyList())) {
        val newsList = remoteShopViewModel.getNewList.value
        value = if (newsList.isEmpty()) {
            return@produceState
        } else {
            Result.success(newsList)
        }
    }
}


@Composable
fun ShopColumn(
    remoteShopViewModel: RemoteShopViewModel,
    title: String,
    list: List<Product>,
    onProductClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .size(20.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            text = title
        )
        ShopLazy(remoteShopViewModel, list = list, onProductClick = onProductClick)
    }
}

@Composable
fun ShopLazy(
    remoteShopViewModel: RemoteShopViewModel,
    list: List<Product>,
    onProductClick: () -> Unit
) {
    LazyRow(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
    ) {
        items(list) { lt ->
            ElevatedCard(
                modifier = Modifier
                    .width(300.dp)
                    .height(170.dp)
                    .padding(5.dp), colors = CardColors(
                    containerColor = White,
                    contentColor = Black,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Gray
                ), onClick = {
                    remoteShopViewModel.addProduct(lt)
                    onProductClick()
                }) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        painter = rememberAsyncImagePainter(model = lt.icon),
                        contentDescription = lt.title
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                        text = lt.title
                    )
                }
            }
        }
    }
}

object DataConstant {
    lateinit var BASE_HOME: BaseHome
    lateinit var BASE_CATEGORY: BaseCategory
}