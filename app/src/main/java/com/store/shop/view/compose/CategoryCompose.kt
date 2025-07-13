package com.store.shop.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.store.shop.viewmodel.RemoteShopViewModel

@Composable
fun CategoryCompose(
    remoteShopViewModel: RemoteShopViewModel,
    onCategoryItemClick: (id: String) -> Unit
) {
    val baseCategories = remoteShopViewModel.getBaseCategories.value!!
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(baseCategories.categories) { categoryItem ->
                ElevatedCard(modifier = Modifier
                    .fillParentMaxWidth()
                    .height(180.dp)
                    .padding(5.dp), colors = CardColors(
                    containerColor = White,
                    contentColor = Black,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Gray
                ), onClick = {
                    onCategoryItemClick(categoryItem.id)
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
                            contentScale = ContentScale.FillBounds,
                            painter = rememberAsyncImagePainter(model = categoryItem.icon),
                            contentDescription = categoryItem.title
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            text = categoryItem.title
                        )
                    }
                }
            }
        }
    }
}