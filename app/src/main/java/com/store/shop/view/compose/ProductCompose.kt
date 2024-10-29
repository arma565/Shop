package com.store.shop.view.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.store.shop.R
import com.store.shop.data.model.Gallery
import com.store.shop.data.model.Product
import com.store.shop.viewmodel.LocalShopViewModel
import com.store.shop.viewmodel.RemoteShopViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductCompose(
    remoteShopViewModel: RemoteShopViewModel,
    localShopViewModel: LocalShopViewModel
) {
    var showFaveIcon by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val product: Product = remoteShopViewModel.product!!
    val galleryList: List<Gallery> = product.gallery
    val pagerState = rememberPagerState(pageCount = {
        if (galleryList.isNotEmpty()) galleryList.size else 0
    })

    val topPadding: Dp = 0.dp
    val heightSize: Dp = 52.dp

    localShopViewModel.getProductList()
    val productList = localShopViewModel.productList.collectAsState().value
    showFaveIcon = productList.isNotEmpty() && productList.any { it.id == product.id }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(15.dp),
                colors = CardColors(
                    containerColor = Color.White,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.White
                )
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    state = pagerState
                ) { page ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (galleryList.isNotEmpty()) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds,
                                painter = rememberAsyncImagePainter(galleryList[page].img),
                                contentDescription = stringResource(id = R.string.product_image)
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
                                        if (pagerState.currentPage == iteration) Color.Black else Color.White
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
            }

            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(15.dp),
                colors = CardColors(
                    containerColor = Color.White,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.White
                )
            ) {

                IconButton(onClick = {
                    if (showFaveIcon) {
                        localShopViewModel.deleteProduct(product.id.toInt())
                        showFaveIcon = false
                    } else {
                        localShopViewModel.upsertProduct(
                            Product(
                                id = product.id,
                                catId = product.catId,
                                catName = product.catName,
                                title = product.title,
                                brand = product.brand,
                                garanti = product.garanti,
                                count = product.count,
                                shortDescription = product.shortDescription,
                                fullDescription = product.fullDescription,
                                special = product.special,
                                discount = product.discount,
                                rate = product.rate,
                                price = product.price,
                                icon = product.icon
                            )
                        )
                        showFaveIcon = true
                    }
                }) {
                    if (showFaveIcon) {
                        FavoriteIcon(Icons.Filled.Favorite)
                    } else {
                        FavoriteIcon(Icons.Outlined.FavoriteBorder)
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(15.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardColors(
                    containerColor = Color.White,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.White
                )
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .verticalScroll(state = scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (product != Product()) {
                        RatingBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            rating = product.rate.toFloat()
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(heightSize)
                                .padding(top = topPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.title),
                                color = Color.Black,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = product.title, color = Color.Black)
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(heightSize)
                                .padding(top = topPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.cat_name),
                                color = Color.Black,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = product.catName, color = Color.Black)
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(heightSize)
                                .padding(top = topPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.brand),
                                color = Color.Black,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = product.brand, color = Color.Black)
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(heightSize)
                                .padding(top = topPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.warranty),
                                color = Color.Black,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = product.garanti, color = Color.Black)
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(heightSize)
                                .padding(top = topPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.inventory),
                                color = Color.Black,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (product.count.toInt() == 0) "Not available" else product.count,
                                color = Color.Black
                            )
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(heightSize)
                                .padding(top = topPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.special),
                                color = Color.Black,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (product.special.toInt() == 0) "No" else "Yes",
                                color = Color.Black
                            )
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(heightSize)
                                .padding(top = topPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.price),
                                color = Color.Black,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = product.price, color = Color.Black)
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(heightSize)
                                .padding(top = topPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.discount),
                                color = Color.Black,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (product.discount.toInt() == 0) "No" else "Yes",
                                color = Color.Black
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(heightSize)
                                .padding(top = topPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.description),
                                color = Color.Black,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                            ExpandableText(
                                text = product.fullDescription
                            )

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteIcon(img: ImageVector) {
    Icon(
        imageVector = img,
        contentDescription = stringResource(id = R.string.favorite),
        tint = Color.Black
    )
}

@Composable
private fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    spaceBetween: Dp = 0.dp
) {

    val image = ImageBitmap.imageResource(id = R.drawable.star)
    val imageFull = ImageBitmap.imageResource(id = R.drawable.full_star)
    val totalCount = 5

    val height = LocalDensity.current.run { image.height.toDp() }
    val width = LocalDensity.current.run { image.width.toDp() }
    val space = LocalDensity.current.run { spaceBetween.toPx() }
    val totalWidth = width * totalCount + spaceBetween * (totalCount - 1)


    Box(
        modifier
            .width(totalWidth)
            .height(height)
            .drawBehind {
                drawRating(rating, image, imageFull, space)
            })
}

private fun DrawScope.drawRating(
    rating: Float,
    image: ImageBitmap,
    imageFull: ImageBitmap,
    space: Float
) {

    val totalCount = 5

    val imageWidth = image.width.toFloat()
    val imageHeight = size.height

    val reminder = rating - rating.toInt()
    val ratingInt = (rating - reminder).toInt()

    for (i in 0 until totalCount) {

        val start = imageWidth * i + space * i

        drawImage(
            image = image,
            topLeft = Offset(start, 0f)
        )
    }

    drawWithLayer {
        for (i in 0 until totalCount) {
            val start = imageWidth * i + space * i
            // Destination
            drawImage(
                image = imageFull,
                topLeft = Offset(start, 0f)
            )
        }

        val end = imageWidth * totalCount + space * (totalCount - 1)
        val start = rating * imageWidth + ratingInt * space
        val size = end - start

        // Source
        drawRect(
            Color.Transparent,
            topLeft = Offset(start, 0f),
            size = Size(size, height = imageHeight),
            blendMode = BlendMode.SrcIn
        )
    }
}

private fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}

const val DEFAULT_MINIMUM_TEXT_LINE = 1

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontStyle: FontStyle? = null,
    text: String,
    collapsedMaxLine: Int = DEFAULT_MINIMUM_TEXT_LINE,
    showMoreText: String = "... Show More",
    showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
    showLessText: String = " Show Less",
    showLessStyle: SpanStyle = showMoreStyle,
    textAlign: TextAlign? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }
    Box(modifier = Modifier
        .clickable(clickable) {
            isExpanded = !isExpanded
        }
        .then(modifier)
    ) {
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildAnnotatedString {
                if (clickable) {
                    if (isExpanded) {
                        append(text)
                        withStyle(style = showLessStyle) { append(showLessText) }
                    } else {
                        val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMoreText.length)
                            .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                        append(adjustText)
                        withStyle(style = showMoreStyle) { append(showMoreText) }
                    }
                } else {
                    append(text)
                }
            },
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            fontStyle = fontStyle,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                }
            },
            style = style,
            textAlign = textAlign,
            color = Color.Black
        )
    }

}