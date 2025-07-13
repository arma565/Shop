package com.store.shop.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.store.shop.data.model.Product
import com.store.shop.viewmodel.LocalShopViewModel
import com.store.shop.viewmodel.RemoteShopViewModel

@Composable
fun FavoriteCompose(
    remoteShopViewModel: RemoteShopViewModel,
    localShopViewModel: LocalShopViewModel,
    onProductClick: () -> Unit
) {
    localShopViewModel.getProductList()
    val faveProductList: List<Product> = localShopViewModel.productList.collectAsState().value
    if (faveProductList.isEmpty())
        return
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top, horizontalArrangement = Arrangement.Start
            ) {
                items(faveProductList) { product ->
                    ElevatedCard(modifier = Modifier
                        .width(300.dp)
                        .height(170.dp)
                        .padding(5.dp), colors = CardColors(
                        containerColor = White,
                        contentColor = Black,
                        disabledContentColor = Color.Gray,
                        disabledContainerColor = Color.Gray
                    ), onClick = {
                        remoteShopViewModel.addProduct(product)
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
                                painter = rememberAsyncImagePainter(model = product.icon),
                                contentDescription = product.title
                            )
                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif,
                                text = product.title
                            )
                        }
                    }
                }
            }
        }
    }
}